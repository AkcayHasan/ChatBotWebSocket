package com.akcay.chatwebsocket.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akcay.chatwebsocket.data.model.Content
import com.akcay.chatwebsocket.data.model.LiveSupportStep
import com.akcay.chatwebsocket.util.Steps

@Composable
fun ChatbotScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatbotViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val currentStep by viewModel.liveSupportStep.collectAsStateWithLifecycle()

    var isProcessStarted by remember { mutableStateOf(false) }


    ChatbotScreenContent(
        uiState = uiState,
        currentStep = currentStep,
        isProcessStarted = isProcessStarted,
        startProcess = {
            isProcessStarted = true
            viewModel.startAllProcess()
        },
        handleUserChoice = { viewModel.handleUserChoice(it) }
    )

}

@Composable
fun ChatbotScreenContent(
    modifier: Modifier = Modifier,
    uiState: ChatbotUiState,
    currentStep: LiveSupportStep?,
    isProcessStarted: Boolean,
    startProcess: () -> Unit,
    handleUserChoice: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (isProcessStarted) {
                when(val content = currentStep?.content) {
                    is Content.ButtonContent -> {
                        Text(
                            text = content.text
                        )
                        content.buttons.forEach { button ->
                            Button(onClick = {
                                handleUserChoice.invoke(button.action)
                            }) {
                                Text(button.label)
                            }
                        }
                    }
                    is Content.SimpleTextContent -> {
                        Text(
                            text = content.content
                        )
                        // TODO: buton eklenebilir ve end conversation işlemi uygulamayı kapatabilir ya da mesaj gösterebiliriz ekranda (step 7 yi de bir butonla ekle)
                        // TODO: belki room eklenerek mesaj geçmişi de gösterilebilir
                    }
                    null -> {}
                }
            } else {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = {
                        startProcess.invoke()
                    }) {
                        Text("Start ChatBot!")
                    }
                }
            }
        }
    }

}

@Composable
@Preview
fun ChatbotScreenPreview(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = {

            }) {
                Text("Start ChatBot!")
            }
        }
    }
}