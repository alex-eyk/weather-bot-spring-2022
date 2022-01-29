package com.alex.eyk.bot.weather.core.handler.command

import com.alex.eyk.bot.weather.core.entity.reply.ReplyProvider
import com.alex.eyk.bot.weather.core.entity.user.UserRepository
import com.alex.eyk.bot.weather.core.handler.AbstractHandler

abstract class CommandHandler<T : java.io.Serializable>(
    val command: String,
    replyProvider: ReplyProvider,
    userRepository: UserRepository
) : AbstractHandler<T>(replyProvider, userRepository)