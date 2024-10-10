package com.akcay.chatwebsocket.di

import com.akcay.chatwebsocket.data.model.LiveSupportStep
import com.akcay.chatwebsocket.data.service.WebSocketClient
import com.akcay.chatwebsocket.util.LiveSupportStepDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    @Singleton
    fun provideGsonObject(): Gson = GsonBuilder()
        .registerTypeAdapter(LiveSupportStep::class.java, LiveSupportStepDeserializer())
        .create()

    @Provides
    @Singleton
    fun provideWebSocketClient(): WebSocketClient {
        return WebSocketClient()
    }
}