package com.akcay.chatwebsocket.domain.repository

import com.akcay.chatwebsocket.data.service.WebSocketClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatbotRepositoryImpl @Inject constructor(
    private val webSocketClient: WebSocketClient
): ChatbotRepository {

    override suspend fun sendStep(stepJson: String) {
        webSocketClient.sendStep(stepJson = stepJson)
    }

    override fun observeMessages(): Flow<String> = webSocketClient.connectFlow()

}