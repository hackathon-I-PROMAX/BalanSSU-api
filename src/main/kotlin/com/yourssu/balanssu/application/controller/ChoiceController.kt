package com.yourssu.balanssu.application.controller

import com.yourssu.balanssu.application.request.MakeChoiceRequest
import com.yourssu.balanssu.application.request.MakeChoiceResponse
import com.yourssu.balanssu.core.security.Authenticated
import com.yourssu.balanssu.core.security.UserInfo
import com.yourssu.balanssu.domain.service.ChoiceService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/choices")
class ChoiceController(private val choiceService: ChoiceService) {
    @ApiOperation("선택지 투표")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun makeChoice(
        @Authenticated userInfo: UserInfo,
        @RequestBody request: MakeChoiceRequest
    ): MakeChoiceResponse {
        val dto = choiceService.makeChoice(userInfo.username, request.categoryId, request.choiceId)
        return MakeChoiceResponse(
            category = dto.categoryDto,
            choices = dto.choiceDtos
        )
    }
}
