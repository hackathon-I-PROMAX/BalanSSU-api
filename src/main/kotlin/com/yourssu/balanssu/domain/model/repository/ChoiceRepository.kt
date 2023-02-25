package com.yourssu.balanssu.domain.model.repository

import com.yourssu.balanssu.domain.model.entity.Choice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChoiceRepository : JpaRepository<Choice, Int> {
    fun findByClientId(clientId: String): Choice?
}
