package com.akcay.chatwebsocket.util

import com.akcay.chatwebsocket.data.model.ButtonOption
import com.akcay.chatwebsocket.data.model.Content
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ContentDeserializer(private val type: String) : JsonDeserializer<Content> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Content {

        return when (type) {
            "button" -> {
                val jsonObject = json.asJsonObject
                val text = jsonObject["text"].asString
                val buttons = context.deserialize<List<ButtonOption>>(
                    jsonObject["buttons"],
                    object : TypeToken<List<ButtonOption>>() {}.type
                )
                Content.ButtonContent(text, buttons)
            }

            else -> {
                val content = if (json.isJsonObject) {
                    json.asJsonObject["content"].asString
                } else {
                    json.asString
                }
                Content.SimpleTextContent(content)
            }
        }
    }
}