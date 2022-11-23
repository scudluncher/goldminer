package com.carrera.goldminer.core.redeemcode.infra.repository

import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode
import com.carrera.goldminer.core.redeemcode.domain.repository.RedeemCodeRepository
import com.carrera.goldminer.core.redeemcode.infra.entity.RedeemCodeJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
class JpaRedeemCodeRepository(private val crudRepository: JpaCrudRedeemCodeRepository) :
    RedeemCodeRepository {
    override fun findByCode(code: String): RedeemCode? {
        return crudRepository.findByCode(code)
            ?.toDomainEntity()
    }

    override fun save(redeemCode: RedeemCode): RedeemCode {
        return crudRepository.save(RedeemCodeJpaEntity(redeemCode))
            .toDomainEntity()
    }

    override fun findByCodeAndRedeemed(code: String, redeemed: Boolean): RedeemCode? {
        return crudRepository.findByCodeAndRedeemed(code, redeemed)
            ?.toDomainEntity()
    }
}

@Component
interface JpaCrudRedeemCodeRepository : JpaRepository<RedeemCodeJpaEntity, Long> {
    fun findByCode(code: String): RedeemCodeJpaEntity?
    fun findByCodeAndRedeemed(code: String, redeemed: Boolean): RedeemCodeJpaEntity?
}
