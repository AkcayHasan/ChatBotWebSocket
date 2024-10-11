package com.akcay.chatwebsocket.domain.usecase

import com.akcay.chatwebsocket.domain.repository.ChatbotRepository
import javax.inject.Inject

class CloseWebSocketUseCase @Inject constructor(
    private val chatbotRepository: ChatbotRepository
) {

    suspend operator fun invoke() {
        chatbotRepository.closeWebSocket()
    }
}