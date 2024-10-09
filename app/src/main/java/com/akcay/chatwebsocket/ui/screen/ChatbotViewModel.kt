package com.akcay.chatwebsocket.ui.screen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akcay.chatwebsocket.data.model.LiveSupportFlow
import com.akcay.chatwebsocket.data.model.LiveSupportStep
import com.akcay.chatwebsocket.domain.usecase.ObserveMessagesUseCase
import com.akcay.chatwebsocket.domain.usecase.SendStepUseCase
import com.google.android.gms.common.internal.LibraryVersion
import com.google.gson.Gson
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
    private val observeMessagesUseCase: ObserveMessagesUseCase
): ViewModel() {

    var uiState = mutableStateOf(ChatbotUiState())
        private set

    private val _liveSupportFlow = MutableStateFlow<LiveSupportFlow?>(null)
    val liveSupportFlow: StateFlow<LiveSupportFlow?> = _liveSupportFlow.asStateFlow()

    private val _liveSupportStep = MutableStateFlow<LiveSupportStep?>(null)
    val liveSupportStep: StateFlow<LiveSupportStep?> = _liveSupportStep.asStateFlow()

    init {
        observeMessages()
        loadJsonFromAssets(context = context)
    }

    private fun observeMessages() {
        viewModelScope.launch {
            observeMessagesUseCase().collect {
                val response = gson.fromJson(it, LiveSupportStep::class.java)
                _liveSupportStep.emit(response)
            }
        }
    }

    private fun handleUserChoice() {
        //sendStepUseCase.invoke()
    }

    private fun loadJsonFromAssets(context: Context) {
        viewModelScope.launch {
            try {
                val flowData = readJsonFromAssets(context, "live_support_flow.json")

                _liveSupportFlow.emit(flowData)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun readJsonFromAssets(context: Context, fileName: String): LiveSupportFlow? {
        return withContext(Dispatchers.IO) {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            gson.fromJson(jsonString, LiveSupportFlow::class.java)
        }
    }
}