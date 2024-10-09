package com.akcay.chatwebsocket.data.service

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class WebSocketClient @Inject constructor(
    private val gson: Gson
) {

    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket

    /*
    private val _incomingMessages = MutableSharedFlow<String>()
    val incomingMessages: SharedFlow<String> = _incomingMessages
     */

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

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

    /*fun connect() {
        val request = Request.Builder().url("wss://echo.websocket.org").build()
        webSocket = client.newWebSocket(request, object: WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                //val response = gson.fromJson(text, LiveSupportStep::class.java)
                scope.launch {
                    _incomingMessages.emit(text)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                scope.launch {
                    _incomingMessages.emit("Error: ${t.message}")
                }
            }
        })
    }*/

    fun sendStep(stepJson: String) {
        val json = gson.toJson(stepJson)
        webSocket.send(json)
    }

    fun close() {
        webSocket.close(1000, null)
        scope.cancel()
    }
}