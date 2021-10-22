package com.pretulmeu.backend.impl.service

import com.pretulmeu.backend.api.dto.PricePerDateDto
import com.pretulmeu.backend.api.dto.ProdusDto
import com.pretulmeu.backend.api.dto.ShopProductDto
import com.pretulmeu.backend.api.service.ProdusService
import com.pretulmeu.backend.api.service.SecurityService
import com.pretulmeu.backend.impl.exceptions.NoExistingProviderException
import com.pretulmeu.backend.impl.exceptions.ProductDoesNotExist
import com.pretulmeu.backend.impl.exceptions.TooManyException
import com.pretulmeu.backend.impl.model.PricePerDate
import com.pretulmeu.backend.impl.model.Produs
import com.pretulmeu.backend.impl.model.ShopProduct
import com.pretulmeu.backend.impl.providers.ShopProvider
import com.pretulmeu.backend.impl.repo.PricePerDateRepo
import com.pretulmeu.backend.impl.repo.ProdusRepo
import com.pretulmeu.backend.impl.repo.ShopProductRepo
import com.pretulmeu.backend.impl.repo.UserRepo
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
internal class ProdusServiceImpl(
    val produsRepo: ProdusRepo,
    val shopProductRepo: ShopProductRepo,
    val pricePerDateRepo: PricePerDateRepo,
    val userRepo: UserRepo,
    val securityService: SecurityService,
    val providers: List<ShopProvider>
) : ProdusService {
    override fun getProductOfUser(): List<ProdusDto> {
        return produsRepo.findAllByUsername(securityService.user?.username).map { it.toDto() }
    }

    class ScrapeThread(val it: ShopProduct,
                       private val providers: List<ShopProvider>,
                       private val toSave: MutableList<PricePerDate>,

                       ): Thread(){
        override fun run() {
            val providerName = ShopProvider.getProviderName(it.link!!)
            val provider = providers.filter { it.name == providerName }
            val ppd = provider[0].getPriceToday(it.link).fromDto(it)
            synchronized(toSave){
                toSave.add(ppd)
            }
        }

        private fun ShopProductDto.fromDto(produs: Produs): ShopProduct {
            return ShopProduct(this.id, this.name, this.link, produs)
                .also { it.history = this.history?.map { ppd -> ppd.fromDto(it) }?.toMutableSet() }
        }

        private fun PricePerDateDto.fromDto(sp: ShopProduct): PricePerDate {
            return PricePerDate(-1, this.price, this.date, sp)
        }
    }



    @Transactional
    override fun saveProduct(url: String) {
        val count = produsRepo.count()
        if(count > 4){
            throw TooManyException("Too many products. You can have max 5 products")
        }
        val providerName = ShopProvider.getProviderName(url)
        val provider = providers.filter { it.name == providerName }
        if(provider.isEmpty()) {
            throw NoExistingProviderException("Website not supported")
        }
        produsRepo.save(
            provider[0].getProdusDto(url)
                .fromDto()
        )
    }

    @Transactional
    override fun deleteProduct(id: Long) {
        val el = produsRepo
            .findById(id)
        if(!el.isPresent || !securityService.checkSameUser(el.get().user)){
            throw ProductDoesNotExist("Product does not exist")
        }
        val prod = el.get()
        prod.shopProducts?.let {
            it.forEach { deleteShopProduct(it) }
        }
        produsRepo.deleteAllByIdInBatch(mutableListOf(id))
    }

    @Transactional
    override fun updateProduct(produsDto: ProdusDto) {
        if(!securityService.checkSameUser(produsDto.fromDto().user)){
            throw ProductDoesNotExist("Product does not exist")
        }
        produsRepo.save(produsDto.fromDto())
    }

    @Transactional
    override fun getProductById(id: Long): ProdusDto {
        val el = produsRepo
            .findById(id)
        if(!el.isPresent || !securityService.checkSameUser(el.get().user)){
            throw ProductDoesNotExist("Product does not exist")
        }
        return el.get().toDto()
    }

    @Transactional
    override fun deleteLink(id: Long) {
        val el = shopProductRepo
            .findById(id)
        if(!el.isPresent || !securityService.checkSameUser(el.get().product.user)){
            throw ProductDoesNotExist("Product does not exist")
        }
        val sp = el.get()
        if(sp.product.shopProducts?.size == 1){
            deleteProduct(sp.product.id)
            return
        }
        deleteShopProduct(sp)
    }

    private fun deleteShopProduct(sp: ShopProduct){
        if(!securityService.checkSameUser(sp.product.user)){
            throw ProductDoesNotExist("Product does not exist")
        }
        sp.history?.let{
            pricePerDateRepo.deleteAllByIdInBatch(it.map { it.id }.toMutableList())
        }
        shopProductRepo.deleteAllByIdInBatch(mutableListOf(sp.id))
    }

    @Scheduled(fixedDelay = 864_00_000) // once a day
    fun schedulePriceTracker() {
        val toSave = mutableListOf<PricePerDate>()
        val ths = mutableListOf<Thread>()
        shopProductRepo.findAll().forEach {
            val x = ScrapeThread(it, providers, toSave)
            x.start()
            ths.add(x)
        }
        ths.forEach { it.join() }
        pricePerDateRepo.saveAll(toSave)
    }

    @Transactional
    override fun addLink(id: Long, url: String) {
        val count = shopProductRepo.count()
        if(count > 7){
            throw TooManyException("Too many links. You can have max 5 links per product")
        }
        val el = produsRepo
            .findById(id)
        if(!el.isPresent || !securityService.checkSameUser(el.get().user)){
            throw ProductDoesNotExist("Product does not exist")
        }
        val prds = el.get()
        val providerName = ShopProvider.getProviderName(url)
        val provider = providers.filter { it.name == providerName }
        if(provider.isEmpty()){
            throw NoExistingProviderException("Website not supported")
        }
        val sp = provider[0].getShopProductDto(url).fromDto(prds)
        shopProductRepo.save(sp)
    }

    override fun getShopProduct(id: Long): ShopProductDto {
        val el = shopProductRepo
            .findById(id)
        if(!el.isPresent || !securityService.checkSameUser(el.get().product.user)){
            throw ProductDoesNotExist("Product does not exist")
        }
        return el.get().toDto()
    }

    private fun ProdusDto.fromDto(): Produs {
        return Produs(
            this.id,
            this.name,
            this.img_name,
            userRepo.findByUsername(securityService.user?.username ?: "Error username")!!
        )
            .also { it.shopProducts = this.history?.map { sp -> sp.fromDto(it) }?.toSet() }
    }
    private fun ShopProductDto.fromDto(produs: Produs): ShopProduct {
        return ShopProduct(this.id, this.name, this.link, produs)
            .also { it.history = this.history?.map { ppd -> ppd.fromDto(it) }?.toMutableSet() }
    }

    private fun PricePerDateDto.fromDto(sp: ShopProduct): PricePerDate {
        return PricePerDate(-1, this.price, this.date, sp)
    }
}


