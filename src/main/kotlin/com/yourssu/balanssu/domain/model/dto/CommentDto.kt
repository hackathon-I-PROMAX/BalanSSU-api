package com.yourssu.balanssu.domain.model.dto

class CommentDto(
    val nickname: String,
    val mbti: String,
    val commentId: String,
    val content: String,
    val isReported: Boolean,
    val isOwner: Boolean,
    val isUserDeleted: Boolean
)
