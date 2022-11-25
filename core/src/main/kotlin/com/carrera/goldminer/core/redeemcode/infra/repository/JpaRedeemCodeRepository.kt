package com.carrera.goldminer.core.redeemcode.infra.repository

import com.carrera.goldminer.core.common.value.PaginatedList
import com.carrera.goldminer.core.common.value.PagingCondition
import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode
import com.carrera.goldminer.core.redeemcode.domain.repository.RedeemCodeRepository
import com.carrera.goldminer.core.redeemcode.infra.entity.RedeemCodeJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import javax.persistence.LockModeType

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

    override fun findValidOneByCodeWithLock(code: String): RedeemCode? {
        return crudRepository.findByCodeAndRedeemedAndExpiredByAfter(code)
            ?.toDomainEntity()
    }

    override fun findAll(pagingCondition: PagingCondition): PaginatedList<RedeemCode> {
        val pageable = if (pagingCondition.page == null || pagingCondition.perPage == null) Pageable.unpaged()
        else PageRequest.of(pagingCondition.page, pagingCondition.perPage)

        val redeemCodes = crudRepository.findAllBy(pageable)
        return PaginatedList(
            redeemCodes.toList()
                .map(RedeemCodeJpaEntity::toDomainEntity),
            redeemCodes.totalElements
        )
    }
}

@Component
interface JpaCrudRedeemCodeRepository : JpaRepository<RedeemCodeJpaEntity, Long> {
    fun findByCode(code: String): RedeemCodeJpaEntity?

    @Lock(LockModeType.OPTIMISTIC)
    fun findByCodeAndRedeemedAndExpiredByAfter(
        code: String,
        redeemed: Boolean = false,
        expiredBy: ZonedDateTime = ZonedDateTime.now(),
    ): RedeemCodeJpaEntity?

    fun findAllBy(pageable: Pageable): Page<RedeemCodeJpaEntity>
}
