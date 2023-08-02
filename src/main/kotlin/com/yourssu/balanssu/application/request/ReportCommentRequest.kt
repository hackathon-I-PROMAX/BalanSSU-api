package com.yourssu.balanssu.application.request

class ReportCommentRequest(
    val commentId: String,
    val type: String,
    val content: String?,
    val email: String
)
