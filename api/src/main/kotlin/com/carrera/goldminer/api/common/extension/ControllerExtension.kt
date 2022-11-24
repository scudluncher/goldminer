package com.carrera.goldminer.api.common.extension

import com.carrera.goldminer.api.common.exception.BadRequestException
import com.carrera.goldminer.api.user.exception.UnauthorizedException
import com.carrera.goldminer.api.user.service.UserService
import com.carrera.goldminer.core.user.domain.entity.User
import org.springframework.security.core.context.SecurityContextHolder

interface ControllerExtension {
    fun userService(): UserService

    fun loggedInUser(): User {
        return SecurityContextHolder.getContext().authentication.principal as User
    }

    fun isAdmin(): Boolean {
        return loggedInUser().isAdmin()
    }

    fun isUser(): Boolean {
        return !isAdmin()
    }

    fun isSameOwnerTo(userId: Long): Boolean {
        return loggedInUser().id == userId
    }

    fun checkExistingUser(userId: Long) {
        userService().findByUserId(userId) ?: throw BadRequestException("해당 유저는 존재하지 않습니다")
    }

    fun checkUserOneSelf(userId: Long) {
        if (isUser() && !isSameOwnerTo(userId)) {
            throw UnauthorizedException()
        }
    }
}
