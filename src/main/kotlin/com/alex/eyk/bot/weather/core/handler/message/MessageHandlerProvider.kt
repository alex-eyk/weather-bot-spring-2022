package com.alex.eyk.bot.weather.core.handler.message

import com.alex.eyk.bot.weather.core.entity.Activity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Collections

@Service
class MessageHandlerProvider @Autowired constructor(handlers: List<MessageHandler>) {

    private val messageHandlers: Map<Activity, MessageHandler>

    init {
        val messageHandlers = HashMap<Activity, MessageHandler>()
        for (handler in handlers) {
            messageHandlers[handler.activity] = handler
        }
        this.messageHandlers = Collections.unmodifiableMap(messageHandlers)
    }

    fun byActivity(activity: Activity): MessageHandler {
        return messageHandlers[activity] ?: throw IllegalStateException("No handler found for activity: $activity")
    }
}