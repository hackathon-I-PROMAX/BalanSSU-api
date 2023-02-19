package com.yourssu.balanssu.application.response

import com.yourssu.balanssu.domain.model.dto.ViewChoiceDto

class ViewChoiceResponse(dto: ViewChoiceDto) {
    val categoryId = dto.categoryId
    val dDay = dto.dDay
    val title = dto.title
    val choices = dto.choices
    val participantCounts = dto.participantCounts
    val isParticipating = dto.isParticipating
    val myChoice = dto.myChoice
}
