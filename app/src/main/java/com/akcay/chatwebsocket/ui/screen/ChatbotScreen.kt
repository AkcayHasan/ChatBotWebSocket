package com.akcay.chatwebsocket.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akcay.chatwebsocket.R.string as AppText
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

    ChatbotScreenContent(
        uiState = uiState,
        currentStep = currentStep,
        startProcess = {
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
    startProcess: () -> Unit,
    handleUserChoice: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (uiState.isProcessStarted) {
                when (val content = currentStep?.content) {
                    is Content.ButtonContent -> {
                        Text(
                            text = content.text,
                            textAlign = TextAlign.Center
                        )
                        content.buttons.forEach { button ->
                            Button(modifier = Modifier.padding(top = 5.dp), onClick = {
                                handleUserChoice.invoke(button.action)
                            }) {
                                Text(button.label)
                            }
                        }
                    }

                    is Content.SimpleTextContent -> {
                        Text(
                            text = content.content,
                            textAlign = TextAlign.Center
                        )
                        Button(onClick = {
                            handleUserChoice.invoke(Steps.Step7.type)
                        }) {
                            Text(text = stringResource(AppText.other_text))
                        }
                    }

                    null -> {}
                }
            } else {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = {
                        startProcess.invoke()
                    }) {
                        Text(text = stringResource(AppText.start_chatbot_text))
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ChatbotScreenPreview(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ChatBot") }
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding(), start = 10.dp, end = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Merhaba, canlı destek hattına hoş geldiniz! Hangi konuda yardım almak istersiniz?",
                textAlign = TextAlign.Center
            )
            Button(modifier = Modifier.padding(top = 5.dp), onClick = {}) {
                Text("Start ChatBot!")
            }
            Button(modifier = Modifier.padding(top = 5.dp), onClick = {}) {
                Text("Start ChatBot!")
            }
            Button(modifier = Modifier.padding(top = 5.dp), onClick = {}) {
                Text("Start ChatBot!")
            }
            Button(modifier = Modifier.padding(top = 5.dp), onClick = {}) {
                Text("Start ChatBot!")
            }
        }
    }
}