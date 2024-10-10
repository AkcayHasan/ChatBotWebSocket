package com.akcay.chatwebsocket.domain.usecase

import com.akcay.chatwebsocket.data.model.LiveSupportStep
import com.akcay.chatwebsocket.domain.repository.ChatbotRepository
import com.google.gson.Gson
import javax.inject.Inject

class SendStepUseCase @Inject constructor(
    private val gson: Gson,
    private val chatbotRepository: ChatbotRepository
) {

    suspend operator fun invoke(stepJson: LiveSupportStep) {
        val json = gson.toJson(stepJson)
        chatbotRepository.sendStep(stepJson = json)
    }
}