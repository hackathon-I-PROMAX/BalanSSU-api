package com.yourssu.balanssu.domain.model.entity

import com.yourssu.balanssu.core.utils.UUIDGenerator
import java.time.Clock
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Table
@Entity
class Report(
    @ManyToOne
    @JoinColumn(nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(nullable = false)
    val comment: Comment,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    val type: String,

    @Column(columnDefinition = "VARCHAR(200)")
    val content: String?,

    @Column(columnDefinition = "VARCHAR(320)", nullable = false)
    val email: String
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    @Column(columnDefinition = "CHAR(36)", nullable = false)
    val clientId: String = UUIDGenerator.generateUUID()

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    val reportedAt = LocalDateTime.now(Clock.systemDefaultZone())

    @Column(columnDefinition = "BIT(1) default 0", nullable = false)
    var isProcessed = false
}
