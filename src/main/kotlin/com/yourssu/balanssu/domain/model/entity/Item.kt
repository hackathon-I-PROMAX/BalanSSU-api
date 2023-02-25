package com.yourssu.balanssu.domain.model.entity

import com.yourssu.balanssu.core.utils.UUIDGenerator
import java.io.File
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table
class Item(
    val path: String,

    val filename: String,

    @ManyToOne
    @JoinColumn(name = "category_id")
    val category: Category
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    val clientId: String = UUIDGenerator.generateUUID()
}
