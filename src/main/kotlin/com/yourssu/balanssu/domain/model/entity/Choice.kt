package com.yourssu.balanssu.domain.model.entity

import com.yourssu.balanssu.core.utils.UUIDGenerator
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table
class Choice(
    @ManyToOne
    @JoinColumn(nullable = false)
    val category: Category,

    @Column(nullable = false)
    val name: String
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    @Column(columnDefinition = "CHAR(36)", nullable = false)
    val clientId: String = UUIDGenerator.generateUUID()
}
