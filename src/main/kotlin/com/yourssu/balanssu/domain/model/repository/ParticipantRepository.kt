package com.yourssu.balanssu.domain.model.repository

import com.yourssu.balanssu.domain.model.entity.Category
import com.yourssu.balanssu.domain.model.entity.Choice
import com.yourssu.balanssu.domain.model.entity.Participant
import com.yourssu.balanssu.domain.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParticipantRepository : JpaRepository<Participant, Int> {
    fun existsByUserAndCategory(user: User, category: Category): Boolean

    fun countByCategoryAndChoice(category: Category, choice: Choice): Int

    fun countByCategory(category: Category): Int

    fun findByUserAndCategory(user: User, category: Category): Participant?
}
