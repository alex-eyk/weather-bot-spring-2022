package com.alex.eyk.bot.weather.core

import com.ximand.properties.PropertiesPath
import com.ximand.properties.Property

@PropertiesPath("jarpath:/app.properties")
class ServerConfig {

    @Property(name = "server.threads", defaultValue = "4")
    var threads = 4

}