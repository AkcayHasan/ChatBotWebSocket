package com.akcay.chatwebsocket.data.service

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class WebSocketClient @Inject constructor() {

    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket

    fun connectFlow(): Flow<String> = callbackFlow {

        val request = Request.Builder().url("wss://echo.websocket.org").build()

        val webSocketListener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                trySend(text)
            }
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                close(t)
            }
        }

        webSocket = client.newWebSocket(request, webSocketListener)

        awaitClose {
            webSocket.close(1000, null)
        }
    }

    fun sendStep(stepJson: String) {
        webSocket.send(stepJson)
    }

    fun close() {
        webSocket.close(1000, null)
    }
}