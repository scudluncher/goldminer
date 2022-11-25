package com.carrera.goldminer.api.gold.controller

import com.carrera.goldminer.api.common.extension.ControllerExtension
import com.carrera.goldminer.api.common.response.SingleResponse
import com.carrera.goldminer.api.gold.request.ChargingGoldWithCodeRequest
import com.carrera.goldminer.api.gold.request.ConsumeGoldRequest
import com.carrera.goldminer.api.gold.service.GoldService
import com.carrera.goldminer.api.gold.viewmodel.CurrentGoldViewModel
import com.carrera.goldminer.api.gold.viewmodel.RedeemResultViewModel
import com.carrera.goldminer.api.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class GoldController(
    private val goldService: GoldService,
    private val userService: UserService,
) : ControllerExtension {
    override fun userService(): UserService {
        return this.userService
    }

    @PostMapping("/users/{userId}/golds/consume")
    fun consumeGold(
        @RequestBody @Valid request: ConsumeGoldRequest,
        @PathVariable userId: Long,
    ): ResponseEntity<SingleResponse<CurrentGoldViewModel>> {
        checkUserOneSelf(userId)
        checkExistingUser(userId)

        val changedGoldResult = goldService.consumeGold(request.toValue(userId))

        return ResponseEntity.ok()
            .body(
                SingleResponse.Ok(CurrentGoldViewModel(changedGoldResult.currentGoldBalanceAmount))
            )
    }

    @GetMapping("/users/{userId}/golds")
    fun currentGoldOf(@PathVariable userId: Long): ResponseEntity<SingleResponse<CurrentGoldViewModel>> {
        checkUserOneSelf(userId)
        checkExistingUser(userId)

        val currentGold = goldService.currentGoldOf(userId)

        return ResponseEntity.ok()
            .eTag(currentGold.version.toString())
            .body(
                SingleResponse.Ok(CurrentGoldViewModel(currentGold.currentGoldBalance))
            )
    }

    @PostMapping("/users/{userId}/golds/earn-by-redeem")
    fun chargeGoldWithRedeem(
        @RequestBody @Valid request: ChargingGoldWithCodeRequest,
        @PathVariable userId: Long,
    ): ResponseEntity<SingleResponse<RedeemResultViewModel>> {
        checkUserOneSelf(userId)
        checkExistingUser(userId)

        val changedGoldResult = goldService.chargeGoldWithCode(request.toValue(userId))

        return ResponseEntity.ok()
            .body(
                SingleResponse.Ok(
                    RedeemResultViewModel(changedGoldResult))
            )
    }
}
