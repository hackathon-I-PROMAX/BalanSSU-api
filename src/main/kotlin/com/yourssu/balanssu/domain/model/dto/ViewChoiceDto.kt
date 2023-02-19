package com.yourssu.balanssu.domain.model.dto

class ViewChoiceDto(
    val categoryId: String,
    val dDay: Int,
    val title: String,
    val choices: List<ChoiceDto>,
    val participantCounts: Int,
    val isParticipating: Boolean,
    val myChoice: String?
)
