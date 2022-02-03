@file:Suppress("unused")

package com.alex.eyk.bot.weather.core.method

import com.alex.eyk.bot.weather.core.entity.reply.Reply
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class SendMessageBuilder {

    private val builder = SendMessage.builder()

    private var markdown: Boolean = false

    fun chat(chat: Long) = apply {
        this.builder.chatId(chat.toString())
    }

    fun reply(reply: Reply): SendMessageBuilder {
        if (reply.format) {
            throw IllegalStateException("This reply must be supplemented with arguments")
        }
        this.markdown = reply.markdown
        this.builder.text(reply.content)
        return this
    }

    fun reply(reply: Reply, vararg args: Any): SendMessageBuilder {
        if (reply.format == false) {
            throw IllegalStateException("This reply should not be format")
        }
        this.markdown = reply.markdown
        this.builder.text(
            reply.content.format(*args)
        )
        return this
    }

    fun build(): SendMessage {
        val sendMessage = builder.build()
        sendMessage.enableMarkdown(markdown)
        return sendMessage
    }

}