package com.carrera.goldminer.api.user.service

import com.carrera.goldminer.api.user.exception.UserNotFoundException
import com.carrera.goldminer.api.user.exception.WrongPasswordException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AuthProvider(
    private val passwordEncoder: PasswordEncoder,
    private val userService: UserService,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val userName = authentication.name
        val password = authentication.credentials.toString()

        val user = userService.findUserByName(userName) ?: throw UserNotFoundException()


        if (!passwordEncoder.matches(password, user.password)) {
            throw WrongPasswordException()
        }

        val authorities = listOf(
            if (user.isAdmin()) SimpleGrantedAuthority(user.type.role)
            else SimpleGrantedAuthority(user.type.role)
        )

        return UsernamePasswordAuthenticationToken(
            user,
            password,
            authorities
        )
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication?.equals(UsernamePasswordAuthenticationToken::class.java)
            ?: false
    }
}

