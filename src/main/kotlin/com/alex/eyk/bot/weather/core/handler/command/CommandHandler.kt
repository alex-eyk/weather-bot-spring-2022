package com.alex.eyk.bot.weather.core.handler.command

import com.alex.eyk.bot.weather.core.handler.AbstractHandler

abstract class CommandHandler(
    val command: String
) : AbstractHandler()