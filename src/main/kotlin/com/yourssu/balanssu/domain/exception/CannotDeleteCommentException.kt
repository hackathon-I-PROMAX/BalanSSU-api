package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.CommentErrorCode
import org.springframework.http.HttpStatus

class CannotDeleteCommentException: CommentException(
    status = HttpStatus.CONFLICT,
    errorCode = CommentErrorCode.CANNOT_DELETE,
    message = "댓글 작성자가 아닙니다."
)
