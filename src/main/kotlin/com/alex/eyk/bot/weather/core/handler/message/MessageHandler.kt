package com.alex.eyk.bot.weather.core.handler.message

import com.alex.eyk.bot.weather.core.entity.Activity
import com.alex.eyk.bot.weather.core.entity.reply.ReplyProvider
import com.alex.eyk.bot.weather.core.entity.user.UserRepository
import com.alex.eyk.bot.weather.core.handler.AbstractHandler

abstract class MessageHandler<T : java.io.Serializable>(
    val activity: Activity,
    replyProvider: ReplyProvider,
    userRepository: UserRepository
) : AbstractHandler<T>(replyProvider, userRepository)