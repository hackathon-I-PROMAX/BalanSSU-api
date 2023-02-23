package com.yourssu.balanssu.domain.service

import com.yourssu.balanssu.domain.exception.CategoryAlreadyExistsException
import com.yourssu.balanssu.domain.exception.CategoryNotFoundException
import com.yourssu.balanssu.domain.exception.UserNotFoundException
import com.yourssu.balanssu.domain.model.dto.CategoryDto
import com.yourssu.balanssu.domain.model.dto.CommentDto
import com.yourssu.balanssu.domain.model.dto.CreateChoiceDto
import com.yourssu.balanssu.domain.model.dto.MainCategoriesDto
import com.yourssu.balanssu.domain.model.dto.ViewCategoriesDto
import com.yourssu.balanssu.domain.model.dto.ViewCategoryDto
import com.yourssu.balanssu.domain.model.entity.Category
import com.yourssu.balanssu.domain.model.enums.CategoryType
import com.yourssu.balanssu.domain.model.repository.CategoryRepository
import com.yourssu.balanssu.domain.model.repository.ParticipantRepository
import com.yourssu.balanssu.domain.model.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDate
import java.time.Period

@Service
@Transactional
class CategoryService(
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val choiceService: ChoiceService,
    private val participantRepository: ParticipantRepository,
) {
    fun viewMainCategories(): MainCategoriesDto {
        val allCategories = categoryRepository.findAll()

        val today = LocalDate.now(Clock.systemDefaultZone())
        val hottestCategories = getHottestCategories(allCategories).map {
            val dDay = Period.between(today, it.deadline).days
            ViewCategoriesDto(
                categoryId = it.clientId,
                title = it.title,
                type = CategoryType.HOTTEST,
                dDay = dDay,
                participantCount = it.participantCount
            )
        }

        val closedCategories = getClosedCategories(allCategories).map {
            val dDay = Period.between(today, it.deadline).days
            ViewCategoriesDto(
                categoryId = it.clientId,
                title = it.title,
                type = CategoryType.CLOSED,
                dDay = dDay,
                participantCount = it.participantCount
            )
        }

        return MainCategoriesDto(hottestCategories, closedCategories)
    }

    fun viewCategories(): List<ViewCategoriesDto> {
        val categories = categoryRepository.findAll()

        val hottestCategories = getHottestCategories(categories)
        val closedCategories = getClosedCategories(categories)
        val openCategories = getOpenCategories(categories, hottestCategories, closedCategories)

        return mergeCategories(hottestCategories, openCategories, closedCategories)
    }

    private fun getHottestCategories(categories: List<Category>): List<Category> {
        val today = LocalDate.now(Clock.systemDefaultZone())
        return categories
            .filter { Period.between(today, it.deadline).days >= 0 }
            .filter { it.participantCount > 0 }
            .sortedWith(compareByDescending<Category> { it.participantCount }.thenByDescending { it.id })
            .take(3)
    }

    private fun getOpenCategories(
        categories: List<Category>,
        hottestCategories: List<Category>,
        closedCategories: List<Category>
    ): List<Category> {
        val excludedCategoryIds = hottestCategories.mapNotNull { it.id } + closedCategories.mapNotNull { it.id }
        return categories
            .filterNot { it.id in excludedCategoryIds }
            .sortedByDescending { it.id }
    }

    private fun getClosedCategories(categories: List<Category>): List<Category> {
        val today = LocalDate.now(Clock.systemDefaultZone())
        return categories
            .filter { Period.between(today, it.deadline).days < 0 }
            .sortedByDescending { it.id }
    }

    private fun mergeCategories(
        hottestCategories: List<Category>,
        openCategories: List<Category>,
        closedCategories: List<Category>
    ): List<ViewCategoriesDto> {
        val today = LocalDate.now()

        fun categoryToDto(category: Category, type: CategoryType): ViewCategoriesDto {
            val dDay = Period.between(today, category.deadline).days
            return ViewCategoriesDto(
                categoryId = category.clientId,
                title = category.title,
                type = type,
                dDay = dDay,
                participantCount = category.participantCount
            )
        }

        return listOf(
            hottestCategories.map { categoryToDto(it, CategoryType.HOTTEST) },
            openCategories.map { categoryToDto(it, CategoryType.OPEN) },
            closedCategories.map { categoryToDto(it, CategoryType.CLOSED) }
        ).flatten()
    }

    fun viewCategory(username: String, categoryId: String): ViewCategoryDto {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException()
        val category = categoryRepository.findByClientId(categoryId) ?: throw CategoryNotFoundException()
        val participant = participantRepository.findByUserAndCategory(user, category)

        val categoryDto = CategoryDto(
            categoryId = category.clientId,
            title = category.title,
            dDay = Period.between(LocalDate.now(Clock.systemDefaultZone()), category.deadline).days,
            participantCount = category.participantCount,
            isParticipating = participant != null,
            myChoice = participant?.choice?.name
        )
        val choicesDto = choiceService.getChoices(category)
        val commentsDto = emptyList<CommentDto>()

        return ViewCategoryDto(
            category = categoryDto,
            choices = choicesDto,
            comments = commentsDto
        )
    }

    fun createCategory(title: String, choiceDtos: List<CreateChoiceDto>) {
        val category = saveCategory(title)
        choiceService.createChoices(category, choiceDtos)
    }

    private fun saveCategory(title: String): Category {
        if (categoryRepository.existsByTitle(title)) {
            throw CategoryAlreadyExistsException()
        }

        val category = Category(title)
        return categoryRepository.save(category)
    }
}
