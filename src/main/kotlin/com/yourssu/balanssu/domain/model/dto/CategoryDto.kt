package com.yourssu.balanssu.domain.model.dto

class CategoryDto(
    val categoryId: String,
    val title: String,
    val dDay: Int,
    val participantCount: Int,
    val isParticipating: Boolean,
    val myChoice: String?
)
