package com.yourssu.balanssu.domain.model.repository

import com.yourssu.balanssu.domain.model.entity.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : JpaRepository<Item, Int> {
    fun findByClientId(clientId: String): Item?
}
