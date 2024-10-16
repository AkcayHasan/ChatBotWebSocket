package com.akcay.chatwebsocket.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akcay.chatwebsocket.data.model.LiveSupportStep
import com.akcay.chatwebsocket.domain.usecase.CloseWebSocketUseCase
import com.akcay.chatwebsocket.domain.usecase.ObserveMessagesUseCase
import com.akcay.chatwebsocket.domain.usecase.SendStepUseCase
import com.akcay.chatwebsocket.util.Constants
import com.akcay.chatwebsocket.util.Steps
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson,
    private val sendStepUseCase: SendStepUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val closeWebSocketUseCase: CloseWebSocketUseCase
) : ViewModel() {

    var uiState = mutableStateOf(ChatbotUiState())
        private set

    private val liveSupportFlow = MutableStateFlow<List<LiveSupportStep>?>(null)

    private val _liveSupportStep = MutableStateFlow<LiveSupportStep?>(null)
    val liveSupportStep: StateFlow<LiveSupportStep?> = _liveSupportStep.asStateFlow()

    init {
        loadJsonFromAssets(context = context)
    }

    private fun observeMessages() {
        viewModelScope.launch {
            observeMessagesUseCase().collect {
                try {
                    val response = gson.fromJson(it, LiveSupportStep::class.java)
                    _liveSupportStep.emit(response)
                    hideLoading()
                } catch (e: Exception) {
                    Log.e("osman", "observeMessages: ${e.message}")
                }
            }
        }
    }

    fun handleUserChoice(buttonAction: String) {
        showLoading()
        if (buttonAction == Steps.EndConversation.type) {
            closeWebSocket()
        } else {
            liveSupportFlow.asStateFlow().value?.forEach { step ->
                if (step.step == buttonAction) {
                    viewModelScope.launch {
                        sendStepUseCase.invoke(step)
                    }
                }
            }
        }
    }

    private fun closeWebSocket() {
        uiState.value = uiState.value.copy(isLoading = false, isProcessStarted = false)
        viewModelScope.launch {
            closeWebSocketUseCase.invoke()
        }
    }

    private fun showLoading() {
        uiState.value = uiState.value.copy(isLoading = true)
    }

    private fun hideLoading() {
        uiState.value = uiState.value.copy(isLoading = false)
    }

    fun startAllProcess() {
        uiState.value = uiState.value.copy(isLoading = true, isProcessStarted = true)
        observeMessages()
        liveSupportFlow.asStateFlow().value?.forEach { step ->
            if (step.step == Steps.Step1.type) {
                viewModelScope.launch {
                    sendStepUseCase.invoke(step)
                }
            }
        }
    }

    private fun loadJsonFromAssets(context: Context) {
        viewModelScope.launch {
            try {
                val flowData = withContext(Dispatchers.IO) {
                    val jsonString = context.assets.open(Constants.JSON_FILE_NAME).bufferedReader()
                        .use { it.readText() }
                    val type = object : TypeToken<List<LiveSupportStep>>() {}.type
                    gson.fromJson<List<LiveSupportStep>>(jsonString, type)
                }
                liveSupportFlow.emit(flowData)
            } catch (e: Exception) {
                Log.e("osman", "${e.message}")
            }
        }
    }
}