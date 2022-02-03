package com.alex.eyk.bot.weather.core.handler.message.activity

import com.alex.eyk.bot.weather.core.entity.Activity
import com.alex.eyk.bot.weather.core.handler.AbstractHandler

abstract class ActivityMessageHandler(
    val activity: Activity
) : AbstractHandler()