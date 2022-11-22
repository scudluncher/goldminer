package com.carerra.goldminer.redeemcode.infra.entity

import com.carerra.goldminer.common.infra.JpaEntity
import com.carerra.goldminer.goldchange.domain.entity.Gold
import com.carerra.goldminer.redeemcode.domain.entity.RedeemCode
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class RedeemCodeJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var code: String,
    var gold: ULong,
    var goldExpiredBy: ZonedDateTime,
    var expiredBy: ZonedDateTime,
    var redeemed: Boolean,
) : JpaEntity<RedeemCode> {
    constructor(redeemCode: RedeemCode) : this(

        redeemCode.id,
        redeemCode.code,
        redeemCode.gold.amount,
        redeemCode.gold.expiredBy,
        redeemCode.expiredBy,
        redeemCode.redeemed
    )

    override fun update(domainEntity: RedeemCode) {
        redeemed = domainEntity.redeemed
    }

    override fun toDomainEntity(): RedeemCode {
        return RedeemCode(
            id,
            code,
            Gold(gold, goldExpiredBy),
            expiredBy,
            redeemed
        )
    }
}
