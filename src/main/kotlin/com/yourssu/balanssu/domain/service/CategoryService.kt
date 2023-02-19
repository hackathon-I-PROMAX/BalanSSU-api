package com.yourssu.balanssu.domain.service

import com.yourssu.balanssu.core.utils.FileUtil
import com.yourssu.balanssu.domain.exception.AlreadyVotedException
import com.yourssu.balanssu.domain.exception.CannotCreateCategoryException
import com.yourssu.balanssu.domain.exception.CategoryNotFoundException
import com.yourssu.balanssu.domain.exception.ItemNotFoundException
import com.yourssu.balanssu.domain.exception.UserNotFoundException
import com.yourssu.balanssu.domain.model.dto.CreateItemDto
import com.yourssu.balanssu.domain.model.dto.ViewChoiceDto
import com.yourssu.balanssu.domain.model.dto.ChoiceDto
import com.yourssu.balanssu.domain.model.entity.Category
import com.yourssu.balanssu.domain.model.entity.Item
import com.yourssu.balanssu.domain.model.entity.Participant
import com.yourssu.balanssu.domain.model.repository.CategoryRepository
import com.yourssu.balanssu.domain.model.repository.ItemRepository
import com.yourssu.balanssu.domain.model.repository.ParticipantRepository
import com.yourssu.balanssu.domain.model.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.time.Clock
import java.time.LocalDate
import java.time.Period

@Service
@Transactional
class CategoryService(
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val itemRepository: ItemRepository,
    private val participantRepository: ParticipantRepository,

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

    fun voteCategory(username: String, categoryId: String, itemId: String): List<ChoiceDto> {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException()
        val category = categoryRepository.findByClientId(categoryId) ?: throw CategoryNotFoundException()
        val item = itemRepository.findByClientId(itemId) ?: throw ItemNotFoundException()

        if (participantRepository.existsByUserAndCategory(user, category)) {
            throw AlreadyVotedException()
        }

        val participant = Participant(
            user = user,
            category = category,
            item = item
        )
        participantRepository.save(participant)

        return getVoteResult(category)
    }

    private fun getVoteResult(category: Category) =
        category.items.map {
            val name = it.name
            val counts = participantRepository.countByCategoryAndItem(category, it)
            ChoiceDto(it.clientId, name, counts)
        }

    fun viewChoices(username: String, categoryId: String): ViewChoiceDto {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException()
        val category = categoryRepository.findByClientId(categoryId) ?: throw CategoryNotFoundException()
        val choices = getVoteResult(category)
        val participantCounts = participantRepository.countByCategory(category)

        val participant = participantRepository.findByUserAndCategory(user, category)
        val myChoice = participant?.item?.name
        return ViewChoiceDto(
            categoryId = category.clientId,
            dDay = Period.between(LocalDate.now(Clock.systemDefaultZone()), category.deadline).days,
            title = category.title,
            choices = choices,
            participantCounts = participantCounts,
            isParticipating = participant != null,
            myChoice = myChoice
        )
    }
}
