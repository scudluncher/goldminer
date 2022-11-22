package com.carerra.goldminer.user.service

import com.carerra.goldminer.user.domain.value.UserType
import com.carerra.goldminer.user.exception.UserNotFoundException
import com.carerra.goldminer.user.exception.WrongPasswordException
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

