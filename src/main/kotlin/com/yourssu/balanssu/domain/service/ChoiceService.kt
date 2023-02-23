package com.yourssu.balanssu.domain.service

import com.yourssu.balanssu.core.utils.FileUtil
import com.yourssu.balanssu.domain.exception.AlreadyMadeChoiceException
import com.yourssu.balanssu.domain.exception.CategoryNotFoundException
import com.yourssu.balanssu.domain.exception.ChoiceNotFoundException
import com.yourssu.balanssu.domain.exception.UserNotFoundException
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
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.time.Clock
import java.time.LocalDate
import java.time.Period
import javax.transaction.Transactional

@Service
@Transactional
class ChoiceService(
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val choiceRepository: ChoiceRepository,
    private val participantRepository: ParticipantRepository,

    @Value("\${application.image.path}")
    private val imagePath: String
) {
    fun createChoices(category: Category, choiceDtos: List<CreateChoiceDto>) {
        uploadChoiceImages(category.title, choiceDtos)
        val choices = saveChoices(category, choiceDtos)
        category.choices = choices
    }

    private fun uploadChoiceImages(title: String, choiceDtos: List<CreateChoiceDto>): List<File> {
        return choiceDtos.map { choice ->
            val image = File(File(imagePath, title), choice.filename)
            FileUtil.convertBase64ToImage(choice.base64, image)
            image
        }
    }

    private fun saveChoices(category: Category, choiceDtos: List<CreateChoiceDto>): List<Choice> {
        val choices = choiceDtos.map { choice ->
            Choice(
                category = category,
                name = choice.name,
                path = category.title,
                filename = choice.filename
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
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException()
        val category = categoryRepository.findByClientId(categoryId) ?: throw CategoryNotFoundException()
        val choice = choiceRepository.findByClientId(choiceId) ?: throw ChoiceNotFoundException()

        val participant = saveChoice(user, category, choice)
        updateCategoryParticipantCount(category)

        val categoryDto = CategoryDto(
            categoryId = category.clientId,
            title = category.title,
            dDay = Period.between(LocalDate.now(Clock.systemDefaultZone()), category.deadline).days,
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
