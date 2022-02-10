package com.alex.eyk.bot.weather.app.handler

import com.alex.eyk.bot.weather.core.entity.reply.impl.Replies
import com.alex.eyk.bot.weather.core.entity.user.User
import com.alex.eyk.bot.weather.core.entity.user.UserRepository
import com.alex.eyk.bot.weather.core.handler.command.CommandHandler
import com.alex.eyk.dictionary.provider.DictionaryProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

const val COMMAND = "start"

@Service
class StartHandler @Autowired constructor(
    private val dictProvider: DictionaryProvider,
    private val userRepository: UserRepository
) : CommandHandler(COMMAND) {

    override fun saveHandle(user: User, message: Message): SendMessage {
        return super.sendSimpleReply(
            user, dictProvider.reply(user.languageCode, Replies.START)
        )
    }

    override fun notRegisteredHandle(message: Message): SendMessage {
        val user = User(message.chatId.toLong(), dictProvider.getDefaultLanguageCode())
        userRepository.save(user)
        return super.sendSimpleReply(
            user, dictProvider.reply(user.languageCode, Replies.START_FIRST_TIME)
        )
    }

}
