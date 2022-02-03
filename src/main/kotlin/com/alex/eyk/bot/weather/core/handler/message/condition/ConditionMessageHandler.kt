package com.alex.eyk.bot.weather.core.handler.message.condition

import com.alex.eyk.bot.weather.core.entity.user.User
import com.alex.eyk.bot.weather.core.handler.AbstractHandler
import org.telegram.telegrambots.meta.api.objects.Message

abstract class ConditionMessageHandler(
    val condition: (User, Message) -> Boolean
) : AbstractHandler()