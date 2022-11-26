package com.carrera.goldminer.api.gold.controller

import com.carrera.goldminer.api.common.ControllerTestExtension
import com.carrera.goldminer.api.common.response.SingleResponse
import com.carrera.goldminer.api.gold.exception.InsufficientGoldException
import com.carrera.goldminer.api.gold.request.ChargingGoldWithCodeRequest
import com.carrera.goldminer.api.gold.request.ConsumeGoldRequest
import com.carrera.goldminer.api.gold.service.GoldService
import com.carrera.goldminer.api.gold.value.CurrentGold
import com.carrera.goldminer.api.gold.value.GoldChangeResult
import com.carrera.goldminer.api.gold.viewmodel.CurrentGoldViewModel
import com.carrera.goldminer.api.gold.viewmodel.RedeemResultViewModel
import com.carrera.goldminer.api.redeemcode.exception.InvalidRedeemCodeException
import com.carrera.goldminer.api.user.service.UserService
import com.carrera.goldminer.core.gold.domain.value.GoldAmount
import com.carrera.goldminer.core.user.domain.entity.User
import com.carrera.goldminer.core.user.domain.value.UserType
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.core.test.TestCase
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
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

@WebMvcTest(GoldController::class)
class GoldControllerTest(
    private val mockMvc: MockMvc,
    @MockBean
    private val goldService: GoldService,
    @MockBean
    private val userService: UserService,
) : ControllerTestExtension, AnnotationSpec() {
    private val objectMapper = objectMapper()

    override suspend fun beforeSpec(spec: Spec) {
        MockitoAnnotations.openMocks(this)
    }

    override suspend fun beforeEach(testCase: TestCase) {
        setUserSecurityContext()
    }

    private fun user(): User {
        return User(
            1,
            "kratos",
            "1234",
            UserType.USER
        )
    }

    @Test
    fun 금_소모_성공() {
        val userId = 1L
        val consumingAmount = 100L
        val request = ConsumeGoldRequest(consumingAmount)
        val content = objectMapper.writeValueAsString(request)

        val goldChangeResult = GoldChangeResult(
            GoldAmount(10000L),
            GoldAmount(consumingAmount)
        )
        val expectedResponse = objectMapper.writeValueAsString(
            SingleResponse.Ok(
                CurrentGoldViewModel(goldChangeResult.currentGoldBalanceAmount)
            )
        )

        val user = user()

        userService.stub {
            on { findByUserId(anyLong()) } doReturn user
        }

        goldService.stub {
            on { consumeGold(any()) } doReturn goldChangeResult
        }

        mockMvc.perform(
            post("/users/{id}/golds/consume", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(content().json(expectedResponse))
            .andDo(print())
    }

    @Test
    fun 금_소모_잔액부족_실패() {
        val userId = 1L
        val consumingAmount = 100L
        val request = ConsumeGoldRequest(consumingAmount)
        val content = objectMapper.writeValueAsString(request)

        val user = user()

        userService.stub {
            on { findByUserId(anyLong()) } doReturn user
        }

        goldService.stub {
            on { consumeGold(any()) } doThrow InsufficientGoldException()
        }

        mockMvc.perform(
            post("/users/{id}/golds/consume", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isConflict)
            .andDo(print())
    }

    @Test
    fun 다른유저_금_소모_실패() {
        val userId = 2L

        val consumingAmount = 100L
        val request = ConsumeGoldRequest(consumingAmount)
        val content = objectMapper.writeValueAsString(request)

        val user = user()

        userService.stub {
            on { findByUserId(anyLong()) } doReturn user
        }

        mockMvc.perform(
            post("/users/{id}/golds/consume", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isForbidden)
            .andDo(print())
    }

    @Test
    fun 금_조회_성공() {
        val userId = 1L
        val user = user()
        val currentGold = CurrentGold(GoldAmount(10000), 100)

        val expectedResponse = objectMapper.writeValueAsString(
            SingleResponse.Ok(CurrentGoldViewModel(currentGold.currentGoldBalance))
        )
        userService.stub {
            on { findByUserId(anyLong()) } doReturn user
        }

        goldService.stub {
            on { currentGoldOf(any()) } doReturn currentGold
        }

        mockMvc.perform(
            get("/users/{userId}/golds", userId)
        )
            .andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    fun 다른유저_금_조회_실패() {
        val userId = 2L
        val user = user()

        userService.stub {
            on { findByUserId(anyLong()) } doReturn user
        }

        mockMvc.perform(
            get("/users/{userId}/golds", userId)
        )
            .andExpect(status().isForbidden)
            .andDo(print())
    }

    @Test
    fun 금_충전_성공() {
        val userId = 1L
        val request = ChargingGoldWithCodeRequest("SOMESTRING")
        val content = objectMapper.writeValueAsString(request)

        val goldChangeResult = GoldChangeResult(
            GoldAmount(10000L),
            GoldAmount(1000L),
            expiredBy = ZonedDateTime.now().plusDays(7)
        )
        val expectedResponse = objectMapper.writeValueAsString(
            SingleResponse.Ok(
                RedeemResultViewModel(goldChangeResult)
            )
        )

        val user = user()

        userService.stub {
            on { findByUserId(anyLong()) } doReturn user
        }

        goldService.stub {
            on { chargeGoldWithCode(any()) } doReturn goldChangeResult
        }

        mockMvc.perform(
            post("/users/{id}/golds/earn-by-redeem", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(content().json(expectedResponse))
            .andDo(print())
    }

    @Test
    fun 금_충전_미발급_코드_실패() {
        val userId = 1L
        val request = ChargingGoldWithCodeRequest("NOVALIDONE")
        val content = objectMapper.writeValueAsString(request)

        val user = user()

        userService.stub {
            on { findByUserId(anyLong()) } doReturn user
        }

        goldService.stub {
            on { chargeGoldWithCode(any()) } doThrow InvalidRedeemCodeException()
        }

        mockMvc.perform(
            post("/users/{id}/golds/earn-by-redeem", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isConflict)
            .andDo(print())
    }

    @Test
    fun 금_충전_코드길이_실패() {
        val userId = 1L
        val request = ChargingGoldWithCodeRequest("TOOOOOOOOOOLONGCODE")
        val content = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            post("/users/{id}/golds/earn-by-redeem", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isBadRequest)
            .andDo(print())
    }
}
