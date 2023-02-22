package com.yourssu.balanssu.domain.service

import com.yourssu.balanssu.domain.exception.AlreadyVotedException
import com.yourssu.balanssu.domain.exception.CategoryAlreadyExistsException
import com.yourssu.balanssu.domain.exception.CategoryNotFoundException
import com.yourssu.balanssu.domain.exception.ChoiceNotFoundException
import com.yourssu.balanssu.domain.exception.UserNotFoundException
import com.yourssu.balanssu.domain.model.dto.ChoiceDto
import com.yourssu.balanssu.domain.model.dto.CreateChoiceDto
import com.yourssu.balanssu.domain.model.dto.MainCategoriesDto
import com.yourssu.balanssu.domain.model.dto.ViewCategoriesDto
import com.yourssu.balanssu.domain.model.dto.ViewChoiceDto
import com.yourssu.balanssu.domain.model.entity.Category
import com.yourssu.balanssu.domain.model.entity.Participant
import com.yourssu.balanssu.domain.model.enums.CategoryType
import com.yourssu.balanssu.domain.model.repository.CategoryRepository
import com.yourssu.balanssu.domain.model.repository.ChoiceRepository
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
    private val choiceRepository: ChoiceRepository,
    private val participantRepository: ParticipantRepository,
) {
    fun viewMainCategories(): MainCategoriesDto {
        val allCategories = categoryRepository.findAll()

        val today = LocalDate.now(Clock.systemDefaultZone())
        val hottestCategories = getHottestCategories(allCategories).map {
            val dDay = Period.between(today, it.deadline).days
            ViewCategoriesDto(it, CategoryType.HOTTEST, dDay)
        }

        val closedCategories = getClosedCategories(allCategories).map {
            val dDay = Period.between(today, it.deadline).days
            ViewCategoriesDto(it, CategoryType.CLOSED, dDay)
        }

        return MainCategoriesDto(hottestCategories, closedCategories)
    }

    fun viewCategories(): List<ViewCategoriesDto> {
        val allCategories = categoryRepository.findAll()
        val hottestCategories = getHottestCategories(allCategories)
        val generalCategories = getGeneralCategories(allCategories, hottestCategories)

        val today = LocalDate.now(Clock.systemDefaultZone())
        return getCategories(hottestCategories, generalCategories)
            .map {
                val dDay = Period.between(today, it.deadline).days
                val categoryType = when {
                    hottestCategories.contains(it) -> CategoryType.HOTTEST
                    dDay >= 0 -> CategoryType.GENERAL
                    else -> CategoryType.CLOSED
                }
                ViewCategoriesDto(it, categoryType, dDay)
            }
    }

    private fun getHottestCategories(categories: List<Category>): List<Category> {
        val today = LocalDate.now(Clock.systemDefaultZone())
        return categories
            .filter { Period.between(today, it.deadline).days >= 0 }
            .filter { it.participantCount > 0 }
            .sortedWith(
                compareByDescending<Category> { it.participantCount }
                    .thenByDescending { it.id }
            )
            .take(3)
    }

    private fun getGeneralCategories(categories: List<Category>, hottestCategories: List<Category>): List<Category> {
        val hottestCategoryIds = hottestCategories.map { it.id!! }.toSortedSet()
        return categories
            .filter { !hottestCategoryIds.contains(it.id) }
            .sortedByDescending { it.id }
    }

    private fun getClosedCategories(categories: List<Category>): List<Category> {
        val today = LocalDate.now(Clock.systemDefaultZone())
        return categories
            .filter { Period.between(today, it.deadline).days < 0 }
            .sortedByDescending { it.id }
            .take(3)
    }

    private fun getCategories(hottestCategories: List<Category>, generalCategories: List<Category>): List<Category> {
        val categories = mutableListOf<Category>()
        categories.addAll(hottestCategories)
        categories.addAll(generalCategories)
        return categories
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

    fun voteCategory(username: String, categoryId: String, choiceId: String): List<ChoiceDto> {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException()
        val category = categoryRepository.findByClientId(categoryId) ?: throw CategoryNotFoundException()
        val choice = choiceRepository.findByClientId(choiceId) ?: throw ChoiceNotFoundException()

        if (participantRepository.existsByUserAndCategory(user, category)) {
            throw AlreadyVotedException()
        }

        val participant = Participant(
            user = user,
            category = category,
            choice = choice
        )
        participantRepository.save(participant)

        category.participantCount = participantRepository.countByCategory(category)
        categoryRepository.save(category)

        return getVoteResult(category)
    }

    private fun getVoteResult(category: Category) =
        category.choices.map {
            val name = it.name
            val counts = participantRepository.countByCategoryAndChoice(category, it)
            ChoiceDto(it.clientId, name, counts)
        }

    fun viewChoices(username: String, categoryId: String): ViewChoiceDto {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException()
        val category = categoryRepository.findByClientId(categoryId) ?: throw CategoryNotFoundException()
        val participant = participantRepository.findByUserAndCategory(user, category)
        val myChoice = participant?.choice?.name

        return ViewChoiceDto(
            categoryId = category.clientId,
            dDay = Period.between(LocalDate.now(Clock.systemDefaultZone()), category.deadline).days,
            title = category.title,
            choices = getVoteResult(category),
            participantCounts = category.participantCount,
            isParticipating = participant != null,
            myChoice = myChoice
        )
    }
}
