package com.carrera.goldminer.api.redeemcode.controller

import com.carrera.goldminer.api.common.request.PagingRequest
import com.carrera.goldminer.api.common.response.ListResponse
import com.carrera.goldminer.api.common.response.Paging
import com.carrera.goldminer.api.common.response.SingleResponse
import com.carrera.goldminer.api.redeemcode.request.RedeemCodeIssueRequest
import com.carrera.goldminer.api.redeemcode.service.RedeemCodeService
import com.carrera.goldminer.api.redeemcode.viewmodel.RedeemCodeIssuedViewModel
import com.carrera.goldminer.api.redeemcode.viewmodel.RedeemCodeViewModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class RedeemCodeController(private val redeemCodeService: RedeemCodeService) {
    @PostMapping("/admin/redeemcodes")
    fun issueRedeemCode(@RequestBody @Valid request: RedeemCodeIssueRequest): ResponseEntity<SingleResponse<RedeemCodeIssuedViewModel>> {
        val redeemCode = redeemCodeService.issueRedeemCode(request.toValue())

        return ResponseEntity(
            SingleResponse.Ok(RedeemCodeIssuedViewModel(redeemCode)),
            HttpStatus.CREATED
        )
    }

    @GetMapping("/admin/redeemcodes")
    fun retrieveRedeemCodes(pagingRequest: PagingRequest): ResponseEntity<ListResponse<RedeemCodeViewModel>> {
        val holder = SecurityContextHolder.getContext()
        println(holder.authentication.authorities.first().authority)
        val redeemCodes = redeemCodeService.retrieveRedeemCodes(pagingRequest.toPagingCondition())
            .translate(::RedeemCodeViewModel)

        return ResponseEntity.ok(
            ListResponse.Ok(
                redeemCodes.items,
                Paging(redeemCodes.totalCount, pagingRequest)
            )
        )
    }
}
