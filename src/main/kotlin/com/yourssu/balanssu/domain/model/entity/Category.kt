package com.yourssu.balanssu.domain.model.entity

import com.yourssu.balanssu.core.utils.UUIDGenerator
import java.time.Clock
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table
class Category(
    @Column(nullable = false)
    val title: String
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    @Column(columnDefinition = "CHAR(36)", nullable = false)
    val clientId: String = UUIDGenerator.generateUUID()

    @Column(columnDefinition = "DATE", nullable = false)
    val deadline: LocalDate = LocalDate.now(Clock.systemDefaultZone()).plusDays(7)

    @OneToMany(mappedBy = "category")
    lateinit var choices: List<Choice>

    @Column(columnDefinition = "INT(10) DEFAULT 0", nullable = false)
    var participantCount: Int = 0
}
