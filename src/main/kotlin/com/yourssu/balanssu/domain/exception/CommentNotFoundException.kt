package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.CommentErrorCode
import org.springframework.http.HttpStatus

class CommentNotFoundException: CommentException(
    status = HttpStatus.BAD_REQUEST,
    errorCode = CommentErrorCode.NOT_FOUND,
    message = "댓글이 존재하지 않습니다."
)
