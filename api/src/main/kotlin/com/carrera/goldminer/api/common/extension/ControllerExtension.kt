package com.carrera.goldminer.api.common.extension

import com.carrera.goldminer.core.user.domain.entity.User
import org.springframework.security.core.context.SecurityContextHolder

interface ControllerExtension {
    fun loggedInUser(): User {
        return SecurityContextHolder.getContext().authentication.principal as User
    }

    fun isAdmin(): Boolean {
        return loggedInUser().isAdmin()
    }

    fun isUser(): Boolean {
        return !isAdmin()
    }

    fun isSameOwnerTo(ownerId: Long): Boolean {
        return loggedInUser().id == ownerId

    }
}
