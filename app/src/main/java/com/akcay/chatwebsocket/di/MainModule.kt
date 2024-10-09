package com.akcay.chatwebsocket.di

import com.akcay.chatwebsocket.data.service.WebSocketClient
import com.google.gson.Gson
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
    fun provideGsonObject() = Gson()

    @Provides
    @Singleton
    fun provideWebSocketClient(gson: Gson): WebSocketClient {
        return WebSocketClient(gson)
    }
}