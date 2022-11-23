package com.carrera.goldminer.api.redeemcode.controller

import com.carrera.goldminer.api.common.response.SingleResponse
import com.carrera.goldminer.api.redeemcode.request.RedeemCodeIssueRequest
import com.carrera.goldminer.api.redeemcode.service.RedeemCodeService
import com.carrera.goldminer.api.redeemcode.viewmodel.RedeemCodeIssuedViewModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@PreAuthorize("hasRole('ADMIN')")
class RedeemCodeController(private val redeemCodeService: RedeemCodeService) {
    @PostMapping("/admin/redeemcodes")
    fun issueRedeemCode(@RequestBody @Valid request: RedeemCodeIssueRequest): ResponseEntity<SingleResponse<RedeemCodeIssuedViewModel>> {
        val redeemCode = redeemCodeService.issueRedeemCode(request.toValue())

        return ResponseEntity(
            SingleResponse.Ok(RedeemCodeIssuedViewModel(redeemCode)), HttpStatus.CREATED
        )
    }
}
