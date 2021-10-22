package com.pretulmeu.backend.api

import com.pretulmeu.backend.api.dto.UserAuthenticationDto
import com.pretulmeu.backend.api.dto.AuthenticationResponseDto
import com.pretulmeu.backend.api.dto.UserDto
import com.pretulmeu.backend.utils.Constants
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping(path = ["${Constants.BASE_PATH}/users"])
interface UserApi{

    @CrossOrigin(origins = ["*"])
    @PostMapping(path = ["/login"])
    fun login(@RequestBody dto: UserAuthenticationDto): AuthenticationResponseDto

    @CrossOrigin(origins = ["*"])
    @PostMapping(path=["/register"])
    fun register(@RequestBody dto: UserAuthenticationDto): AuthenticationResponseDto


    @GetMapping(path = ["/test"])
    fun test(): String

    @CrossOrigin(origins = ["*"])
    @GetMapping(path=["/current"])
    fun getUser(): UserDto?
}