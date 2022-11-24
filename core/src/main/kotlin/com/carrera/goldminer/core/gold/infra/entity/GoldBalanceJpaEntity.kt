package com.carrera.goldminer.core.gold.infra.entity

import com.carrera.goldminer.core.common.infra.JpaEntity
import com.carrera.goldminer.core.gold.domain.entity.GoldBalance
import com.carrera.goldminer.core.gold.domain.value.GoldAmount
import javax.persistence.*

@Entity(name = "gold_balance")
class GoldBalanceJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    val userId: Long,
    var goldAmount: Long,
    @Version
    var version: Long = 1L,
) : JpaEntity<GoldBalance> {
    constructor(goldBalance: GoldBalance) : this(
        goldBalance.id,
        goldBalance.userId,
        goldBalance.gold.amount,
        goldBalance.version
    )

    override fun update(domainEntity: GoldBalance) {
        goldAmount = domainEntity.gold.amount
    }

    override fun toDomainEntity(): GoldBalance {
        return GoldBalance(
            this.id,
            this.userId,
            GoldAmount(goldAmount),
            version
        )
    }
}
