package com.carrera.goldminer.api.user.service

import com.carrera.goldminer.core.user.domain.entity.User
import com.carrera.goldminer.core.user.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun findUserByName(userName: String): User? {
        return userRepository.findByName(userName)
    }

    fun findByUserId(id: Long): User? {
        return userRepository.findById(id)
    }
}
