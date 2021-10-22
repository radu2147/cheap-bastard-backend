package com.pretulmeu.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
@ComponentScan(basePackages = [
	"com.pretulmeu.backend.*",
	"com.pretulmeu.backend.impl.controller",
	"com.pretulmeu.backend.impl.repo",
	"com.pretulmeu.backend.impl.service",
	"com.pretulmeu.backend.impl.model",
	"com.pretulmeu.backend.api",

])
@EnableJpaRepositories
@EnableScheduling
class BackendApplication

fun main(args: Array<String>) {
	runApplication<BackendApplication>(*args)
}