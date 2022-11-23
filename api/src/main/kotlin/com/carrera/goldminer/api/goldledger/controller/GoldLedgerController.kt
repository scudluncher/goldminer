package com.carrera.goldminer.api.goldledger.controller

import com.carrera.goldminer.api.common.extension.ControllerExtension
import com.carrera.goldminer.api.common.response.SingleResponse
import com.carrera.goldminer.api.goldledger.request.ConsumeGoldRequest
import com.carrera.goldminer.api.goldledger.service.GoldLedgerService
import com.carrera.goldminer.api.goldledger.viewmodel.CurrentGoldViewModel
import com.carrera.goldminer.api.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class GoldLedgerController(
    private val goldLedgerService: GoldLedgerService,
    private val userService: UserService,
) : ControllerExtension {
    override fun userService(): UserService {
        return this.userService
    }

    @PatchMapping("/goldledgers")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    fun consumeGold(@RequestBody @Valid request: ConsumeGoldRequest): ResponseEntity<SingleResponse<CurrentGoldViewModel>> {
        checkExistingUser(request.ownerId)
        checkUserOneSelf(request.ownerId)

        val currentGold = goldLedgerService.consumeGold(request.toValue())

        return ResponseEntity(
            SingleResponse.Ok(CurrentGoldViewModel(currentGold)),
            HttpStatus.OK
        )
    }

    @GetMapping("/goldledgers/users/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    fun currentGoldOf(@PathVariable userId: Long): ResponseEntity<SingleResponse<CurrentGoldViewModel>> {
        checkExistingUser(userId)
        checkUserOneSelf(userId)

        val currentGold = goldLedgerService.currentGoldOf(userId)

        return ResponseEntity(
            SingleResponse.Ok(CurrentGoldViewModel(currentGold)),
            HttpStatus.OK
        )
    }
}
