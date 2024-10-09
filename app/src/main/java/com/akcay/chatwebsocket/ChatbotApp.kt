package com.akcay.chatwebsocket

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.akcay.chatwebsocket.ui.screen.ChatbotScreen
import com.akcay.chatwebsocket.ui.theme.ChatWebSocketTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotApp(modifier: Modifier = Modifier) {
    ChatWebSocketTheme {

        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("ChatBot") }
                )
            }
        ) {
            Box(modifier = modifier.padding(it)) {
                ChatbotScreen()
            }
        }
    }
}