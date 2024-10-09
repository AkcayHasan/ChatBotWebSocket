package com.akcay.chatwebsocket.domain.usecase

import com.akcay.chatwebsocket.domain.repository.ChatbotRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMessagesUseCase @Inject constructor(
    private val chatbotRepository: ChatbotRepository
) {

    operator fun invoke(): Flow<String> {
        return chatbotRepository.observeMessages()
    }
}