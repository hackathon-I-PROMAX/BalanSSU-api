package com.yourssu.balanssu.domain.service

import com.yourssu.balanssu.core.utils.FileUtil
import com.yourssu.balanssu.domain.exception.CannotCreateCategoryException
import com.yourssu.balanssu.domain.model.dto.CreateItemDto
import com.yourssu.balanssu.domain.model.entity.Category
import com.yourssu.balanssu.domain.model.entity.Item
import com.yourssu.balanssu.domain.model.repository.CategoryRepository
import com.yourssu.balanssu.domain.model.repository.ItemRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File

@Service
@Transactional
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val itemRepository: ItemRepository,
    @Value("\${application.image.path}")
    private val imagePath: String
) {
    fun createCategory(title: String, itemDtos: List<CreateItemDto>) {
        if (categoryRepository.existsByTitle(title)) {
            throw CannotCreateCategoryException()
        }

        val category = Category(title)
        categoryRepository.save(category)

        val items = itemDtos.map { dto ->
            val image = File(File(imagePath, title), dto.filename)
            FileUtil.convertBase64ToImage(dto.base64, image)

            Item(category, dto.name, image.parent, image.name)
        }
        itemRepository.saveAll(items)

        category.items = items
    }
}
