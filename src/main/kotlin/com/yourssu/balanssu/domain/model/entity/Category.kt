package com.yourssu.balanssu.domain.model.entity

import com.yourssu.balanssu.core.utils.UUIDGenerator
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table
class Category(
    val title: String
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    val clientId: String = UUIDGenerator.generateUUID()

    val deadline: LocalDate = LocalDate.now().plusDays(7)

    @OneToMany(mappedBy = "category")
    lateinit var items: List<Item>

    @OneToOne
    @JoinColumn(name = "first_place")
    lateinit var firstPlace: Item
}
