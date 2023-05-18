package com.yourssu.balanssu.domain.service

import com.yourssu.balanssu.core.utils.DDayCalculator
import com.yourssu.balanssu.domain.exception.CategoryAlreadyExistsException
import com.yourssu.balanssu.domain.exception.CategoryNotFoundException
import com.yourssu.balanssu.domain.exception.UserNotFoundException
import com.yourssu.balanssu.domain.model.dto.CategoryDto
import com.yourssu.balanssu.domain.model.dto.CreateChoiceDto
import com.yourssu.balanssu.domain.model.dto.MainCategoriesDto
import com.yourssu.balanssu.domain.model.dto.ViewCategoriesDto
import com.yourssu.balanssu.domain.model.dto.ViewCategoryDto
import com.yourssu.balanssu.domain.model.entity.Category
import com.yourssu.balanssu.domain.model.enums.CategoryType
import com.yourssu.balanssu.domain.model.repository.CategoryRepository
import com.yourssu.balanssu.domain.model.repository.ParticipantRepository
import com.yourssu.balanssu.domain.model.repository.UserRepository
import java.time.Clock
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CategoryService(
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val choiceService: ChoiceService,
    private val participantRepository: ParticipantRepository,
) {
    fun viewMainCategories(): MainCategoriesDto {
        val today = LocalDate.now(Clock.systemDefaultZone())
        val allCategories = categoryRepository.findAll()

        val hotCategories = getHotCategories(today, allCategories).map {
            val dDay = DDayCalculator.getDDay(today, it.deadline)
            ViewCategoriesDto(
                categoryId = it.clientId,
                title = it.title,
                type = CategoryType.HOT,
                dDay = dDay,
                participantCount = it.participantCount
            )
        }

        val closedCategories = getClosedCategories(today, allCategories)
            .take(3)
            .map {
                val dDay = ChronoUnit.DAYS.between(today, it.deadline)
                ViewCategoriesDto(
                    categoryId = it.clientId,
                    title = it.title,
                    type = CategoryType.CLOSED,
                    dDay = dDay,
                    participantCount = it.participantCount
                )
            }

        return MainCategoriesDto(hotCategories, closedCategories)
    }

    fun viewCategories(): List<ViewCategoriesDto> {
        val today = LocalDate.now(Clock.systemDefaultZone())
        val categories = categoryRepository.findAll()

        val hotCategories = getHotCategories(today, categories)
        val closedCategories = getClosedCategories(today, categories)
        val openCategories = getOpenCategories(categories, hotCategories, closedCategories)

        return mergeCategories(hotCategories, openCategories, closedCategories)
    }

    private fun getHotCategories(today: LocalDate, categories: List<Category>): List<Category> {
        return categories
            .filter { DDayCalculator.getDDay(today, it.deadline) >= 0 }
            .filter { it.participantCount > 0 }
            .sortedWith(compareByDescending<Category> { it.participantCount }.thenByDescending { it.id })
            .take(3)
    }

    private fun getOpenCategories(
        categories: List<Category>,
        hotCategories: List<Category>,
        closedCategories: List<Category>
    ): List<Category> {
        val excludedCategoryIds = hotCategories.mapNotNull { it.id } + closedCategories.mapNotNull { it.id }
        return categories
            .filterNot { it.id in excludedCategoryIds }
            .sortedByDescending { it.id }
    }

    private fun getClosedCategories(today: LocalDate, categories: List<Category>): List<Category> {
        return categories
            .filter { DDayCalculator.getDDay(today, it.deadline) < 0 }
            .sortedByDescending { it.id }
    }

    private fun mergeCategories(
        hotCategories: List<Category>,
        openCategories: List<Category>,
        closedCategories: List<Category>
    ): List<ViewCategoriesDto> {
        val today = LocalDate.now(Clock.systemDefaultZone())

        fun categoryToDto(category: Category, type: CategoryType): ViewCategoriesDto {
            val dDay = ChronoUnit.DAYS.between(today, category.deadline)
            return ViewCategoriesDto(
                categoryId = category.clientId,
                title = category.title,
                type = type,
                dDay = dDay,
                participantCount = category.participantCount
            )
        }

        return listOf(
            hotCategories.map { categoryToDto(it, CategoryType.HOT) },
            openCategories.map { categoryToDto(it, CategoryType.OPEN) },
            closedCategories
                .map { categoryToDto(it, CategoryType.CLOSED) }
                .filter { it.dDay in (-3..-1) }
        ).flatten()
    }

    fun viewCategory(username: String, categoryId: String): ViewCategoryDto {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException()
        val category = categoryRepository.findByClientId(categoryId) ?: throw CategoryNotFoundException()
        val participant = participantRepository.findByUserAndCategory(user, category)

        val categoryDto = CategoryDto(
            categoryId = category.clientId,
            title = category.title,
            dDay = ChronoUnit.DAYS.between(LocalDate.now(Clock.systemDefaultZone()), category.deadline),
            participantCount = category.participantCount,
            isParticipating = participant != null,
            myChoice = participant?.choice?.clientId
        )
        val choicesDto = choiceService.getChoices(category)

        return ViewCategoryDto(categoryDto, choicesDto)
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
