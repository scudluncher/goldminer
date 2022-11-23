package com.carerra.goldminer.redeemcode.usecase

import com.carerra.goldminer.goldchange.domain.value.Gold
import com.carerra.goldminer.redeemcode.domain.entity.RedeemCode
import com.carerra.goldminer.redeemcode.domain.repository.RedeemCodeRepository
import java.time.ZonedDateTime
import kotlin.random.Random

class RedeemCodeIssuing(
    private val value: RedeemCodeIssueValue,
    private val redeemCodeRepository: RedeemCodeRepository,
) {
    fun execute(): RedeemCode {
        val code = notDuplicatedRandomCode()
        val redeemCode = RedeemCode(
            code = code,
            gold = Gold(value.amount, value.goldExpiredBy),
            expiredBy = value.codeExpiredBy
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
