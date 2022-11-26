package com.carrera.goldminer.api.redeemcode.controller

import com.carrera.goldminer.api.SecurityConfig
import com.carrera.goldminer.api.common.ControllerTestExtension
import com.carrera.goldminer.api.redeemcode.service.RedeemCodeService
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.AnnotationSpec
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(RedeemCodeController::class)
@Import(SecurityConfig::class)
class RedeemCodeControllerTestWhenNotAdmin(
    private val mockMvc: MockMvc,
    @MockBean
    private val redeemCodeService: RedeemCodeService,
) : ControllerTestExtension, AnnotationSpec() {

    override suspend fun beforeSpec(spec: Spec) {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    @WithMockUser
    fun 일반유저_발급_실패() {
        mockMvc.perform(
            post("/admin/redeemcodes")
                .with(csrf())
        )
            .andExpect(status().isForbidden)
            .andDo(print())
    }

    @Test
    @WithAnonymousUser
    fun 비로그인유저_발급_실패() {
        mockMvc.perform(
            post("/admin/redeemcodes")
                .with(csrf())
        )
            .andExpect(status().isForbidden)
            .andDo(print())
    }

    @Test
    @WithMockUser
    fun 일반유저_조회_실패() {
        mockMvc.perform(
            get("/admin/redeemcodes")
                .with(csrf())
        )
            .andExpect(status().isForbidden)
            .andDo(print())
    }

    @Test
    @WithAnonymousUser
    fun 비로그인유저_조회_실패() {
        mockMvc.perform(
            get("/admin/redeemcodes")
                .with(csrf())
        )
            .andExpect(status().isForbidden)
            .andDo(print())
    }
}




