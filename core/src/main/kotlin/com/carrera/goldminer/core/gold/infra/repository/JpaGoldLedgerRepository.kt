package com.carrera.goldminer.core.gold.infra.repository

import com.carrera.goldminer.core.gold.domain.entity.GoldLedger
import com.carrera.goldminer.core.gold.domain.repository.GoldLedgerRepository
import com.carrera.goldminer.core.gold.infra.entity.GoldLedgerJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

@Repository
class JpaGoldLedgerRepository(private val crudRepository: JpaCrudGoldLedgerRepository) : GoldLedgerRepository {
    override fun save(goldLedger: GoldLedger): GoldLedger {
        return crudRepository.save(GoldLedgerJpaEntity(goldLedger))
            .toDomainEntity()
    }

    override fun findValidOneByUserIdOrderByExpired(userId: Long): List<GoldLedger> {
        return crudRepository.findByUserIdAndGoldNotExpiredAndNotAllConsumedOrderByExpiredBy(
            userId,
            ZonedDateTime.now()
        )
            .map { it.toDomainEntity() }
    }
}

@Component
interface JpaCrudGoldLedgerRepository : JpaRepository<GoldLedgerJpaEntity, Long> {
    @Query("""
        select l from gold_ledger l
        where l.userId = :userId
        and l.chargedGoldExpiredBy > :now
        and l.chargedGoldAmount <> l.usedGoldAmount 
        order by l.chargedGoldExpiredBy
        """)
    fun findByUserIdAndGoldNotExpiredAndNotAllConsumedOrderByExpiredBy(
        @Param("userId") userId: Long,
        @Param("now") now: ZonedDateTime,
    ): List<GoldLedgerJpaEntity>
}
