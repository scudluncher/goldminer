package com.carrera.goldminer.core.user.domain.entity

import com.carrera.goldminer.core.user.domain.value.UserType

class User(
    val id: Long = 0L,
    val userName: String,
    val password: String,
    val type: UserType,
) {
    fun isAdmin(): Boolean {
        return type == UserType.ADMIN
    }
}
