package com.akcay.chatwebsocket.util

import com.akcay.chatwebsocket.data.model.Content
import com.akcay.chatwebsocket.data.model.LiveSupportStep
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class LiveSupportStepDeserializer : JsonDeserializer<LiveSupportStep> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LiveSupportStep {
        val jsonObject = json.asJsonObject

        val step = jsonObject["step"].asString
        val type = jsonObject["type"].asString
        val action = jsonObject["action"].asString

        val contentDeserializer = ContentDeserializer(type)
        val content =
            contentDeserializer.deserialize(jsonObject["content"], Content::class.java, context)

        return LiveSupportStep(step, type, content, action)
    }
}