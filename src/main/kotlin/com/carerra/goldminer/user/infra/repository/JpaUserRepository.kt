package com.carerra.goldminer.user.infra.repository

import com.carerra.goldminer.user.domain.entity.User
import com.carerra.goldminer.user.domain.repository.UserRepository
import com.carerra.goldminer.user.infra.entity.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
class JpaUserRepository(private val crudUserRepository: JpaCrudUserRepository) : UserRepository {
    override fun findByName(userName: String): User? {
        return crudUserRepository.findByUserName(userName)
            ?.toDomainEntity()
    }

    override fun save(user: User): User {
        return crudUserRepository.save(UserJpaEntity(user))
            .toDomainEntity()
    }
}

@Component
interface JpaCrudUserRepository : JpaRepository<UserJpaEntity, Long> {
    fun findByUserName(userName: String): UserJpaEntity?
}


