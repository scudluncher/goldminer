package com.carerra.goldminer.goldledger.controller

import com.carerra.goldminer.common.extension.ControllerExtension
import com.carerra.goldminer.common.response.SingleResponse
import com.carerra.goldminer.goldledger.request.ConsumeGoldRequest
import com.carerra.goldminer.goldledger.service.GoldLedgerService
import com.carerra.goldminer.goldledger.viewmodel.CurrentGoldViewModel
import com.carerra.goldminer.user.exception.UnauthorizedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class GoldLedgerController(private val goldLedgerService: GoldLedgerService) : ControllerExtension {
    @PatchMapping("/goldledgers")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    fun consumeGold(@RequestBody @Valid request: ConsumeGoldRequest): ResponseEntity<SingleResponse<CurrentGoldViewModel>> {
        if (isUser() && !isSameOwnerTo(request.ownerId)) {
            throw UnauthorizedException()
        }

        val currentGold = goldLedgerService.consumeGold(request.toValue())

        return ResponseEntity(
            SingleResponse.Ok(CurrentGoldViewModel(currentGold)), HttpStatus.OK
        )
    }

//    @GetMapping("/goldledgers/users/{userId}")
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
//    fun getCurrentGold(@PathVariable userId: Long): ResponseEntity<SingleResponse<CurrentGoldViewModel>> {
//        if (isUser() && !isSameOwnerTo(userId)) {
//            throw UnauthorizedException()
//        }
//
//
//    }
}
