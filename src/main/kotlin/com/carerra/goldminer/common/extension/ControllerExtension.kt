package com.carerra.goldminer.common.extension

import com.carerra.goldminer.user.domain.entity.User
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
