package com.carrera.goldminer.api.redeemcode.service

import com.carrera.goldminer.api.redeemcode.exception.CodeRedeemConcurrencyException
import com.carrera.goldminer.api.redeemcode.usecase.ChargingGoldWithCode
import com.carrera.goldminer.api.redeemcode.usecase.ChargingValue
import com.carrera.goldminer.api.redeemcode.usecase.RedeemCodeIssueValue
import com.carrera.goldminer.api.redeemcode.usecase.RedeemCodeIssuing
import com.carrera.goldminer.core.goldledger.domain.entity.GoldLedger
import com.carrera.goldminer.core.goldledger.domain.repository.GoldLedgerRepository
import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode
import com.carrera.goldminer.core.redeemcode.domain.repository.RedeemCodeRepository
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RedeemCodeService(
    private val redeemCodeRepository: RedeemCodeRepository,
    private val goldLedgerRepository: GoldLedgerRepository,
) {
    @Transactional
    fun issueRedeemCode(value: RedeemCodeIssueValue): RedeemCode {
        return RedeemCodeIssuing(
            value,
            redeemCodeRepository
        )
            .execute()
    }

    @Transactional
    fun chargeGoldWithCode(value: ChargingValue): GoldLedger {
        try {
            return ChargingGoldWithCode(
                value,
                goldLedgerRepository,
                redeemCodeRepository,
            )
                .execute()

        } catch (e: ObjectOptimisticLockingFailureException) {
            println("code redeem overlap")
            throw CodeRedeemConcurrencyException(e) // TODO () 예외 전환이 왜 안되는거같지..?
        }
    }
}
