package com.yourssu.balanssu.domain.service

import com.yourssu.balanssu.core.utils.DDayCalculator
import com.yourssu.balanssu.domain.exception.AlreadyMadeChoiceException
import com.yourssu.balanssu.domain.exception.CategoryNotFoundException
import com.yourssu.balanssu.domain.exception.ChoiceNotFoundException
import com.yourssu.balanssu.domain.exception.RestrictedUserException
import com.yourssu.balanssu.domain.model.dto.CategoryDto
import com.yourssu.balanssu.domain.model.dto.ChoiceDto
import com.yourssu.balanssu.domain.model.dto.CreateChoiceDto
import com.yourssu.balanssu.domain.model.dto.MakeChoiceDto
import com.yourssu.balanssu.domain.model.entity.Category
import com.yourssu.balanssu.domain.model.entity.Choice
import com.yourssu.balanssu.domain.model.entity.Participant
import com.yourssu.balanssu.domain.model.entity.User
import com.yourssu.balanssu.domain.model.repository.CategoryRepository
import com.yourssu.balanssu.domain.model.repository.ChoiceRepository
import com.yourssu.balanssu.domain.model.repository.ParticipantRepository
import com.yourssu.balanssu.domain.model.repository.UserRepository
import java.time.Clock
import java.time.LocalDate
import javax.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class ChoiceService(
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val choiceRepository: ChoiceRepository,
    private val participantRepository: ParticipantRepository,
) {
    fun createChoices(category: Category, choiceDtos: List<CreateChoiceDto>) {
        val choices = saveChoices(category, choiceDtos)
        category.choices = choices
    }

    private fun saveChoices(category: Category, choiceDtos: List<CreateChoiceDto>): List<Choice> {
        val choices = choiceDtos.map { choice ->
            Choice(
                category = category,
                name = choice.name
            )
        }
        return choiceRepository.saveAll(choices)
    }

    fun getChoices(category: Category): List<ChoiceDto> {
        return category.choices.map {
            ChoiceDto(
                choiceId = it.clientId,
                name = it.name,
                count = participantRepository.countByCategoryAndChoice(category, it)
            )
        }
    }

    fun makeChoice(username: String, categoryId: String, choiceId: String): MakeChoiceDto {
        val user = userRepository.findByUsernameAndIsDeletedIsFalse(username) ?: throw RestrictedUserException()
        val category = categoryRepository.findByClientId(categoryId) ?: throw CategoryNotFoundException()
        val choice = choiceRepository.findByClientId(choiceId) ?: throw ChoiceNotFoundException()

        val participant = saveChoice(user, category, choice)
        updateCategoryParticipantCount(category)

        val today = LocalDate.now(Clock.systemDefaultZone())
        val categoryDto = CategoryDto(
            categoryId = category.clientId,
            title = category.title,
            dDay = DDayCalculator.getDDay(today, category.deadline),
            participantCount = category.participantCount,
            isParticipating = true,
            myChoice = participant.choice.name
        )
        val choicesDto = getChoices(category)
        return MakeChoiceDto(categoryDto, choicesDto)
    }

    private fun saveChoice(user: User, category: Category, choice: Choice): Participant {
        if (participantRepository.existsByUserAndCategory(user, category)) {
            throw AlreadyMadeChoiceException()
        }

        val participant = Participant(
            user = user,
            category = category,
            choice = choice
        )
        return participantRepository.save(participant)
    }

    private fun updateCategoryParticipantCount(category: Category) {
        category.participantCount = participantRepository.countByCategory(category)
        categoryRepository.save(category)
    }
}
