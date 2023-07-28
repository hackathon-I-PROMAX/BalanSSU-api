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
import org.hibernate.annotations.Where

@Entity
@Table
@Where(clause = "is_deleted = 0")
class Comment(
    @ManyToOne
    @JoinColumn(name = "category_id")
    val category: Category,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    val content: String
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    @Column(columnDefinition = "CHAR(36)", nullable = false)
    val clientId: String = UUIDGenerator.generateUUID()

    @Column(columnDefinition = "BIT(1)", nullable = false)
    var isDeleted: Boolean = false

    @Column(columnDefinition = "DATETIME")
    var deletedAt: LocalDateTime? = null

    fun delete() {
        this.isDeleted = true
        this.deletedAt = LocalDateTime.now(Clock.systemDefaultZone())
    }
}
