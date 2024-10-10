package com.akcay.chatwebsocket.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akcay.chatwebsocket.data.model.LiveSupportStep
import com.akcay.chatwebsocket.domain.usecase.ObserveMessagesUseCase
import com.akcay.chatwebsocket.domain.usecase.SendStepUseCase
import com.akcay.chatwebsocket.util.Constants
import com.akcay.chatwebsocket.util.Steps
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson,
    private val sendStepUseCase: SendStepUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase
) : ViewModel() {

    var uiState = mutableStateOf(ChatbotUiState())
        private set

    private val _liveSupportFlow = MutableStateFlow<List<LiveSupportStep>?>(null)
    private val liveSupportFlow: StateFlow<List<LiveSupportStep>?> = _liveSupportFlow.asStateFlow()

    private val _liveSupportStep = MutableStateFlow<LiveSupportStep?>(null)
    val liveSupportStep: StateFlow<LiveSupportStep?> = _liveSupportStep.asStateFlow()

    init {
        observeMessages()
        loadJsonFromAssets(context = context)
    }

    private fun observeMessages() {
        viewModelScope.launch {
            observeMessagesUseCase().collect {
                try {
                    hideLoading()
                    val response = gson.fromJson(it, LiveSupportStep::class.java)
                    _liveSupportStep.emit(response)
                } catch (e: Exception) {
                    Log.e("osman", "observeMessages: ${e.message}")
                }
            }
        }
    }

    fun handleUserChoice(buttonAction: String) {
        if (buttonAction == Steps.EndConversation.type) {
            closeWebSocket()
        } else {
            liveSupportFlow.value?.forEach { step ->
                if (step.step == buttonAction) {
                    viewModelScope.launch {
                        showLoading()
                        sendStepUseCase.invoke(step)
                    }
                }
            }
        }
    }

    private fun closeWebSocket() {

    }

    private fun showLoading() {
        uiState.value = uiState.value.copy(isLoading = true)
    }

    private fun hideLoading() {
        uiState.value = uiState.value.copy(isLoading = false)
    }

    fun startAllProcess() {
        liveSupportFlow.value?.forEach { step ->
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
                val jsonString = context.assets.open(Constants.JSON_FILE_NAME).bufferedReader()
                    .use { it.readText() }
                val type = object : TypeToken<List<LiveSupportStep>>() {}.type
                val flowData = gson.fromJson<List<LiveSupportStep>>(jsonString, type)
                _liveSupportFlow.emit(flowData)
            } catch (e: Exception) {
                Log.e("osman", "${e.message}")
            }
        }
    }
}