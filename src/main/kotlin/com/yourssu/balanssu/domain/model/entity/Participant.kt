package com.yourssu.balanssu.domain.model.entity

import java.time.Clock
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Table
@Entity
class Participant(
    @OneToOne
    @JoinColumn(nullable = false)
    val category: Category,

    @OneToOne
    @JoinColumn(nullable = false)
    val user: User,

    @OneToOne
    @JoinColumn(nullable = false)
    val choice: Choice
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    val participatedAt = LocalDateTime.now(Clock.systemDefaultZone())
}
