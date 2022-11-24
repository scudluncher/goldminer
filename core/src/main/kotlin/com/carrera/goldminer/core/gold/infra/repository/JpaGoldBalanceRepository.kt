package com.carrera.goldminer.core.gold.infra.repository

import com.carrera.goldminer.core.gold.domain.entity.GoldBalance
import com.carrera.goldminer.core.gold.domain.repository.GoldBalanceRepository
import com.carrera.goldminer.core.gold.infra.entity.GoldBalanceJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
class JpaGoldBalanceRepository(private val crudRepository: JpaCrudGoldBalanceRepository) : GoldBalanceRepository {
    override fun save(goldBalance: GoldBalance): GoldBalance {
        return crudRepository.save(GoldBalanceJpaEntity(goldBalance))
            .toDomainEntity()
    }

    override fun findByUserId(userId: Long): GoldBalance? {
        return crudRepository.findByUserId(userId)
            ?.toDomainEntity()
    }
}

@Component
interface JpaCrudGoldBalanceRepository : JpaRepository<GoldBalanceJpaEntity, Long> {
    fun findByUserId(userId: Long): GoldBalanceJpaEntity?
}
