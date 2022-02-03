package com.alex.eyk.bot.weather.core.method

import com.alex.eyk.bot.weather.core.entity.reply.Reply
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

var replyProperties: MutableMap<String, Boolean> = HashMap()

var SendMessage.SendMessageBuilder.markdown: Boolean
    get() = replyProperties["markdown"] ?: false
    set(value) {
        replyProperties["markdown"] = value
    }

fun SendMessage.SendMessageBuilder.chat(chat: Long): SendMessage.SendMessageBuilder {
    return this.chatId(chat.toString())
}

fun SendMessage.SendMessageBuilder.reply(reply: Reply): SendMessage.SendMessageBuilder {
    if (reply.format) {
        throw IllegalStateException("Reply: $reply should be format")
    }
    this.markdown = reply.markdown
    return this.text(reply.content)
}

fun SendMessage.SendMessageBuilder.build(useExt: Boolean): SendMessage {
    if (useExt == false) {
        return this.build()
    }
    val sendMessage = this.build()
    sendMessage.enableMarkdown(this.markdown)
    return sendMessage
}
