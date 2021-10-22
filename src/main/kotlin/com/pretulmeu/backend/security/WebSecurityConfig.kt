package com.pretulmeu.backend.security

import com.pretulmeu.backend.security.filters.AuthorizationFilter
import com.pretulmeu.backend.utils.Constants
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableWebSecurity
@Configuration
internal class WebSecurityConfig(val jwtToken: AuthorizationFilter, val userDetailsService: UserDetailsService): WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.cors()?.and()?.csrf()?.disable()?.authorizeRequests()
            ?.antMatchers("/${Constants.BASE_PATH}/users/register", "/${Constants.BASE_PATH}/users/current", "/${Constants.BASE_PATH}/users/login")?.anonymous()
            ?.anyRequest()?.authenticated()
            ?.and()
        http?.addFilterBefore(jwtToken, UsernamePasswordAuthenticationFilter::class.java)
        http?.sessionManagement()
            ?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

    }


    @Bean
    fun bCryptPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun getAuthenticationManager(): AuthenticationManager{
        return super.authenticationManager()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val source = UrlBasedCorsConfigurationSource()

        val configuration = CorsConfiguration().applyPermitDefaultValues()
        configuration.allowedOrigins = mutableListOf("*")
	configuration.allowedHeaders = mutableListOf("*")
        configuration.allowedMethods = mutableListOf("GET", "POST", "DELETE", "PUT")
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}