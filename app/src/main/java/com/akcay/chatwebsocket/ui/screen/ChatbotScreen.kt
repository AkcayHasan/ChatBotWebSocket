package com.akcay.chatwebsocket.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ChatbotScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatbotViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState



    ChatbotScreenContent(
        uiState = uiState
    )

}

@Composable
fun ChatbotScreenContent(
    modifier: Modifier = Modifier,
    uiState: ChatbotUiState
) {


}

@Composable
@Preview
fun ChatbotScreenPreview(modifier: Modifier = Modifier) {
    Column {

    }
}