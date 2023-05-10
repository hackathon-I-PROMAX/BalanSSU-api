package com.yourssu.balanssu.domain.model.entity

import com.yourssu.balanssu.core.utils.UUIDGenerator
import java.time.Clock
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table
class Category(
    val title: String
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    val clientId: String = UUIDGenerator.generateUUID()

    val deadline: LocalDate = LocalDate.now(Clock.systemDefaultZone()).plusDays(7)

    @OneToMany(mappedBy = "category")
    lateinit var choices: List<Choice>

    var participantCount: Int = 0
}
