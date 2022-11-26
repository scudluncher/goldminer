package com.carrera.goldminer.api.redeemcode.controller

import com.carrera.goldminer.api.common.ControllerTestExtension
import com.carrera.goldminer.api.common.request.PagingRequest
import com.carrera.goldminer.api.common.response.ListResponse
import com.carrera.goldminer.api.common.response.Paging
import com.carrera.goldminer.api.common.response.SingleResponse
import com.carrera.goldminer.api.redeemcode.request.RedeemCodeIssueRequest
import com.carrera.goldminer.api.redeemcode.service.RedeemCodeService
import com.carrera.goldminer.api.redeemcode.viewmodel.RedeemCodeIssuedViewModel
import com.carrera.goldminer.api.redeemcode.viewmodel.RedeemCodeViewModel
import com.carrera.goldminer.core.common.value.PaginatedList
import com.carrera.goldminer.core.gold.domain.value.ChargedGold
import com.carrera.goldminer.core.gold.domain.value.GoldAmount
import com.carrera.goldminer.core.redeemcode.domain.entity.RedeemCode
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.core.test.TestCase
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.ZonedDateTime

@WebMvcTest(RedeemCodeController::class)
class RedeemCodeControllerTestDefault(
    private val mockMvc: MockMvc,
    @MockBean
    private val redeemCodeService: RedeemCodeService,
) : ControllerTestExtension, AnnotationSpec() {

    private val objectMapper = objectMapper()

    override suspend fun beforeSpec(spec: Spec) {
        MockitoAnnotations.openMocks(this)
    }

    override suspend fun beforeEach(testCase: TestCase) {
        setAdminSecurityContext()
    }

    private val aMonthLater = ZonedDateTime.now().plusMonths(1)
    private val aWeekLater = ZonedDateTime.now().plusWeeks(1)
    private val twoWeeksLater = ZonedDateTime.now().plusWeeks(2)
    private val aWeekBefore = ZonedDateTime.now().minusWeeks(1)

    @Test
    fun 이슈코드_어드민_정상발급() {
        val amount = 10000L
        val goldExpiredBy = aMonthLater
        val codeExpiredBy = aMonthLater
        val request = RedeemCodeIssueRequest(
            amount,
            goldExpiredBy,
            codeExpiredBy
        )
        val content = objectMapper.writeValueAsString(request)

        val redeemCode = RedeemCode(
            code = "SOMESTRING",
            includedChargedGold = ChargedGold(GoldAmount(10000L), goldExpiredBy),
            expiredBy = codeExpiredBy,
        )
        val expectedResponse = objectMapper.writeValueAsString(
            SingleResponse.Ok(
                RedeemCodeIssuedViewModel(redeemCode)
            )
        )

        redeemCodeService.stub {
            on { issueRedeemCode(any()) } doReturn redeemCode
        }

        mockMvc.perform(
            post("/admin/redeemcodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(expectedResponse))
            .andDo(print())
    }

    @Test
    fun 만기_과거로_지정하여_발급실패() {
        val amount = 10000L
        val goldExpiredBy = aWeekBefore
        val codeExpiredBy = aWeekBefore
        val request = RedeemCodeIssueRequest(
            amount,
            goldExpiredBy,
            codeExpiredBy
        )
        val content = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            post("/admin/redeemcodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isBadRequest)
            .andDo(print())
    }

    @Test
    fun 코드만기가_골드만기보다_길게_설정_발급실패() {
        val amount = 10000L
        val goldExpiredBy = aWeekLater
        val codeExpiredBy = twoWeeksLater
        val request = RedeemCodeIssueRequest(
            amount,
            goldExpiredBy,
            codeExpiredBy
        )
        val content = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            post("/admin/redeemcodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isBadRequest)
            .andDo(print())
    }

    @Test
    fun 코드전체조회() {
        val amount = 10000L
        val goldExpiredBy = aMonthLater
        val codeExpiredBy = aMonthLater

        val redeemCodes = listOf(
            RedeemCode(
                code = "SOMESTRING",
                includedChargedGold = ChargedGold(GoldAmount(amount), goldExpiredBy),
                expiredBy = codeExpiredBy,
            ),
            RedeemCode(
                code = "STRINGSOME",
                includedChargedGold = ChargedGold(GoldAmount(amount), goldExpiredBy),
                expiredBy = codeExpiredBy,
            )
        )

        val resultList = PaginatedList(redeemCodes, redeemCodes.size)
        val expectedResponse = objectMapper.writeValueAsString(
            ListResponse.Ok(
                resultList.translate(::RedeemCodeViewModel).items,
                Paging(resultList.totalCount, PagingRequest(null, null))
            )
        )

        redeemCodeService.stub {
            on { retrieveRedeemCodes(any()) } doReturn resultList
        }

        mockMvc.perform(
            get("/admin/redeemcodes")
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(content().json(expectedResponse))
            .andDo(print())
    }
}




