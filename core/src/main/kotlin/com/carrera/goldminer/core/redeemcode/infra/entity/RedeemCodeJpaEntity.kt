package com.carrera.goldminer.core.redeemcode.infra.entity

import com.carrera.goldminer.core.common.infra.JpaEntity
import com.carrera.goldminer.core.goldledger.domain.value.Gold
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
    var version: Long? = null,
) : JpaEntity<RedeemCode> {
    constructor(redeemCode: RedeemCode) : this(
        redeemCode.id,
        redeemCode.code,
        redeemCode.includedGold.amount,
        redeemCode.includedGold.expiredBy,
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
            Gold(gold, goldExpiredBy),
            expiredBy,
            redeemed,
            version
        )
    }
}
