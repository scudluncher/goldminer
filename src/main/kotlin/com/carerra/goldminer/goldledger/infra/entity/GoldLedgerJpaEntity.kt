package com.carerra.goldminer.goldledger.infra.entity

import com.carerra.goldminer.common.infra.JpaEntity
import com.carerra.goldminer.goldledger.domain.entity.GoldLedger
import com.carerra.goldminer.goldledger.domain.value.Gold
import com.carerra.goldminer.goldledger.domain.value.GoldAmount
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity (name = "gold_ledger")
class GoldLedgerJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    var ownerId: Long,
    var chargedGoldAmount: Long,
    var chargedGoldExpiredBy: ZonedDateTime,
    var usedGoldAmount: Long,
) : JpaEntity<GoldLedger> {
    constructor(goldLedger: GoldLedger) :this(
        goldLedger.id,
        goldLedger.ownerId,
        goldLedger.chargedGold.amount,
        goldLedger.chargedGold.expiredBy,
        goldLedger.usedGold.amount
    )
    override fun update(domainEntity: GoldLedger) {
        usedGoldAmount = domainEntity.usedGold.amount
    }

    override fun toDomainEntity(): GoldLedger {
        return GoldLedger(
            this.id,
            this.ownerId,
            Gold(chargedGoldAmount, chargedGoldExpiredBy),
            GoldAmount(usedGoldAmount
            )
        )
    }
}
