package com.carrera.goldminer.core.redeemcode.infra.entity

import com.carrera.goldminer.core.common.infra.JpaEntity
import com.carrera.goldminer.core.gold.domain.value.ChargedGold
import com.carrera.goldminer.core.gold.domain.value.GoldAmount
import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode
import java.time.ZonedDateTime
import javax.persistence.*

@Entity(name = "redeem_code")
class RedeemCodeJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var code: String,
    var gold: Long,
    var goldExpiredBy: ZonedDateTime,
    var expiredBy: ZonedDateTime,
    var redeemed: Boolean,
    @Version
    var version: Long = 1L,
) : JpaEntity<RedeemCode> {
    constructor(redeemCode: RedeemCode) : this(
        redeemCode.id,
        redeemCode.code,
        redeemCode.includedChargedGold.gold.amount,
        redeemCode.includedChargedGold.expiredBy,
        redeemCode.expiredBy,
        redeemCode.redeemed,
        redeemCode.version
    )

    override fun update(domainEntity: RedeemCode) {
        redeemed = domainEntity.redeemed
    }

    override fun toDomainEntity(): RedeemCode {
        return RedeemCode(
            id,
            code,
            ChargedGold(GoldAmount(gold), goldExpiredBy),
            expiredBy,
            redeemed,
            version
        )
    }
}
