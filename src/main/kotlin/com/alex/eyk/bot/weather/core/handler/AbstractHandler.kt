package com.alex.eyk.bot.weather.core.handler

import com.alex.eyk.bot.weather.core.entity.user.User
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

abstract class AbstractHandler {

    fun handle(user: User, message: Message): BotApiMethod<*> {
        if (user.enabled) {
            return saveHandle(user, message)
        } else {
            throw IllegalStateException("User: ${user.chat} was disabled")
        }
    }

    abstract fun saveHandle(user: User, message: Message): BotApiMethod<*>

    fun buildTask(): TaskBuilder {
        return TaskBuilder()
    }

    inner class TaskBuilder internal constructor() {

        private lateinit var user: User
        private lateinit var message: Message
        private lateinit var onResult: (BotApiMethod<*>) -> Unit
        private lateinit var onError: (Throwable) -> Unit

        fun user(user: User) = apply { this.user = user }

        fun message(message: Message) = apply { this.message = message }

        fun onResult(onResult: (BotApiMethod<out java.io.Serializable>) -> Unit) = apply { this.onResult = onResult }

        fun onError(onError: (Throwable) -> Unit) = apply { this.onError = onError }

        fun build(): Runnable {
            return Runnable {
                try {
                    onResult.invoke(handle(user, message))
                } catch (t: Throwable) {
                    onError.invoke(t)
                }
            }
        }

    }
}
