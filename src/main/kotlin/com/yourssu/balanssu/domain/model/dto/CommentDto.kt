package com.yourssu.balanssu.domain.model.dto

class CommentDto(
    val nickname: String,
    val department: String,
    val commentId:String,
    val content: String,
    val isOwner: Boolean,
    val isUserDeleted: Boolean
)
