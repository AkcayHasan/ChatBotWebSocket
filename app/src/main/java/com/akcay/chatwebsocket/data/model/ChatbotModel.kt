package com.akcay.chatwebsocket.data.model

data class LiveSupportFlow(
    val steps: List<LiveSupportStep>
)

data class LiveSupportStep(
    val step: String,
    val type: String,
    val content: Content,
    val action: String
)

data class Content(
    val text: String,
    val buttons: List<ButtonOption>?
)

data class ButtonOption(
    val label: String,
    val action: String
)