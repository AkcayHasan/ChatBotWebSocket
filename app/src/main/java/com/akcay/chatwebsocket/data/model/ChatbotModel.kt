package com.akcay.chatwebsocket.data.model

import com.google.gson.annotations.SerializedName

data class LiveSupportStep(
    @SerializedName("step")
    val step: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("content")
    val content: Content,
    @SerializedName("action")
    val action: String
)

sealed class Content {
    data class ButtonContent(
        val text: String,
        val buttons: List<ButtonOption>
    ) : Content()

    data class SimpleTextContent(
        val content: String
    ) : Content()
}

data class ButtonOption(
    @SerializedName("label")
    val label: String,
    @SerializedName("action")
    val action: String
)