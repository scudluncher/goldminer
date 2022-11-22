package com.carerra.goldminer.user.domain.repository

import com.carerra.goldminer.user.domain.entity.User
import com.carerra.goldminer.user.domain.value.UserType

interface UserRepository {
    fun findByName(userName: String): User?
    fun save(user: User): User
}

class FakeUserRepository : UserRepository {
    private val users: MutableList<User> = mutableListOf(
        User(
            id = 1,
            userName = "kratos",
            password = "MTIzNA==",  // 1234
            type = UserType.USER
        ),
        User(
            id = 2,
            userName = "mimir",
            password = "MTIzNA==", // 1234
            type = UserType.USER
        ),
        User(
            id = 3,
            userName = "freya",
            password = "MTIzNA==", // 1234
            type = UserType.USER
        ),
        User(
            id = 4,
            userName = "atreus",
            password = "NDMyMQ==", // 4321
            type = UserType.ADMIN
        )
    )

    override fun findByName(userName: String): User? {
        return users.firstOrNull { it.userName == userName }
    }

    override fun save(user: User): User {
        if (user.id == 0L) {
            val newUser = User(
                (users.maxOfOrNull { it.id } ?: 0) + 1,
                user.userName,
                user.password,
                user.type,
            )
            users.add(newUser)

            return newUser
        } else {
            users.removeIf { it.id == user.id }
            users.add(user)

            return user
        }
    }
}
