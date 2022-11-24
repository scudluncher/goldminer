package com.carrera.goldminer.api.redeemcode.usecase

import com.carrera.goldminer.core.gold.domain.value.ChargedGold
import com.carrera.goldminer.core.gold.domain.value.GoldAmount
import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode
import com.carrera.goldminer.core.redeemcode.domain.repository.RedeemCodeRepository
import java.time.ZonedDateTime
import kotlin.random.Random

class RedeemCodeIssuing(
    private val value: RedeemCodeIssueValue,
    private val redeemCodeRepository: RedeemCodeRepository,
) {
    fun execute(): RedeemCode {
        val code = notDuplicatedRandomCode()
        val goldAmount = GoldAmount(value.amount)
        val redeemCode = RedeemCode(
            code = code,
            includedChargedGold = ChargedGold(goldAmount, value.goldExpiredBy),
            expiredBy = value.codeExpiredBy,
        )

        return redeemCodeRepository.save(redeemCode)
    }

    private fun notDuplicatedRandomCode(): String {
        val randomCode = randomCode()
        return redeemCodeRepository.findByCode(randomCode)?.let {
            notDuplicatedRandomCode()
        } ?: randomCode
    }
}

class RedeemCodeIssueValue(
    val amount: Long,
    val goldExpiredBy: ZonedDateTime,
    val codeExpiredBy: ZonedDateTime,
)

fun randomCode(): String {
    val charPool: List<Char> = ('A'..'Z') + ('0'..'9')

    return (1..10).map { Random.nextInt(0, charPool.size).let { charPool[it] } }
        .joinToString("")
}
