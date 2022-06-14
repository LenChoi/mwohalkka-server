package com.wedo.mwohalkka.server.admin.security

import com.wedo.mwohalkka.server.admin.service.AdminService
import com.wedo.mwohalkka.server.admin.admin.domain.AdminUser
import mu.KotlinLogging
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AdminAuthenticationProvider(
    val adminService: AdminService,
    val passwordEncoder: PasswordEncoder,
    val env: Environment
) : AuthenticationProvider {
    val logger = KotlinLogging.logger {}

    override fun authenticate(authentication: Authentication): Authentication {
        val splitCredentials = authentication.credentials.toString().split("|")
        val password = splitCredentials[0]
        val userOtpNumber = splitCredentials[1]
        val loadUser = adminService.loadUserByUsername(authentication.name) as AdminUser

        if (!passwordEncoder.matches(password, loadUser.password)) {
            throw SecurityException()
        }

        return UsernamePasswordAuthenticationToken(loadUser, null, loadUser.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean =
        authentication == UsernamePasswordAuthenticationToken::class.java
}
