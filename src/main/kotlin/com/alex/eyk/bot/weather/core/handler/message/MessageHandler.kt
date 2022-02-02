package com.alex.eyk.bot.weather.core.handler.message

import com.alex.eyk.bot.weather.core.entity.Activity
import com.alex.eyk.bot.weather.core.handler.AbstractHandler

abstract class MessageHandler(
    val activity: Activity
) : AbstractHandler()