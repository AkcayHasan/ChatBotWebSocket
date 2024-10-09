package com.akcay.chatwebsocket.di

import com.akcay.chatwebsocket.domain.repository.ChatbotRepository
import com.akcay.chatwebsocket.domain.repository.ChatbotRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideChatbotRepositoryImpl(impl: ChatbotRepositoryImpl): ChatbotRepository

}