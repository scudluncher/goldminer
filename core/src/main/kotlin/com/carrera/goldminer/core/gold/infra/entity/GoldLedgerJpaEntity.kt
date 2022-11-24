package com.carrera.goldminer.core.gold.infra.entity

import com.carrera.goldminer.core.common.infra.JpaEntity
import com.carrera.goldminer.core.gold.domain.entity.GoldLedger
import com.carrera.goldminer.core.gold.domain.value.ChargedGold
import com.carrera.goldminer.core.gold.domain.value.GoldAmount
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "gold_ledger")
class GoldLedgerJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var userId: Long,
    var chargedGoldAmount: Long,
    var chargedGoldExpiredBy: ZonedDateTime,
    var usedGoldAmount: Long,
) : JpaEntity<GoldLedger> {
    constructor(goldLedger: GoldLedger) : this(
        goldLedger.id,
        goldLedger.userId,
        goldLedger.chargedGold.gold.amount,
        goldLedger.chargedGold.expiredBy,
        goldLedger.usedGold.amount
    )

    override fun update(domainEntity: GoldLedger) {
        usedGoldAmount = domainEntity.usedGold.amount
    }

    override fun toDomainEntity(): GoldLedger {
        return GoldLedger(
            this.id,
            this.userId,
            ChargedGold(GoldAmount(chargedGoldAmount), chargedGoldExpiredBy),
            GoldAmount(usedGoldAmount
            )
        )
    }
}
