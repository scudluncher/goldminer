package com.carerra.goldminer.common.initializer

import com.carerra.goldminer.user.domain.entity.User
import com.carerra.goldminer.user.domain.repository.UserRepository
import com.carerra.goldminer.user.domain.value.UserType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class BasicInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @PostConstruct
    fun init() {
        val users = listOf(
            User(
                id = 1,
                userName = "kratos",
                password = passwordEncoder.encode("1234"),
                type = UserType.USER
            ),
            User(
                id = 2,
                userName = "mimir",
                password = passwordEncoder.encode("1234"),
                type = UserType.USER
            ),
            User(
                id = 3,
                userName = "freya",
                password = passwordEncoder.encode("1234"),
                type = UserType.USER
            ),
            User(
                id = 4,
                userName = "atreus",
                password = passwordEncoder.encode("1234"),
                type = UserType.ADMIN
            )
        )

        users.map { userRepository.save(it) }
    }

}
