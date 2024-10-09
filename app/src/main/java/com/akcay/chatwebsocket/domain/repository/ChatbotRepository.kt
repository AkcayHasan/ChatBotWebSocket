package com.akcay.chatwebsocket.domain.repository

import kotlinx.coroutines.flow.Flow

interface ChatbotRepository {

    suspend fun sendStep(stepJson: String)
    fun observeMessages(): Flow<String>
}