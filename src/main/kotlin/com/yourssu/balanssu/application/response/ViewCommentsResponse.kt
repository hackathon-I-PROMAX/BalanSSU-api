package com.yourssu.balanssu.application.response

import com.yourssu.balanssu.domain.model.dto.CommentDto
import org.springframework.data.domain.Page

class ViewCommentsResponse(val comments: Page<CommentDto>)
