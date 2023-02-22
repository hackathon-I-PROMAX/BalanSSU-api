package com.yourssu.balanssu.domain.service

import com.yourssu.balanssu.core.utils.FileUtil
import com.yourssu.balanssu.domain.model.dto.CreateChoiceDto
import com.yourssu.balanssu.domain.model.entity.Category
import com.yourssu.balanssu.domain.model.entity.Choice
import com.yourssu.balanssu.domain.model.repository.ChoiceRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import javax.transaction.Transactional

@Service
@Transactional
class ChoiceService(
    private val choiceRepository: ChoiceRepository,

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
}
