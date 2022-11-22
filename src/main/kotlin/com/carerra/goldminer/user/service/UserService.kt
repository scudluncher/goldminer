package com.carerra.goldminer.user.service

import com.carerra.goldminer.user.domain.entity.User
import com.carerra.goldminer.user.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun findUserByName(userName: String): User? {
        return userRepository.findByName(userName)
    }
}
