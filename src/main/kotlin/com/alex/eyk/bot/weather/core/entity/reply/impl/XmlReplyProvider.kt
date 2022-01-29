package com.alex.eyk.bot.weather.core.entity.reply.impl

import com.alex.eyk.bot.weather.core.entity.reply.Reply
import com.alex.eyk.bot.weather.core.entity.reply.ReplyProvider
import com.alex.eyk.bot.weather.core.xml.impl.ReplyXmlParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class XmlReplyProvider @Autowired constructor(xmlRepliesParser: ReplyXmlParser) : ReplyProvider {

    @Value("\${replies.path}")
    private lateinit var repliesPath: String

    private val replies: Map<String, Reply>

    init {
        try {
            javaClass.classLoader.getResourceAsStream(repliesPath)
                .use { xmlInputStream ->
                    if (xmlInputStream != null) {
                        this.replies = xmlRepliesParser.parse(xmlInputStream)
                    } else {
                        throw IllegalStateException("File with replies not found, path: $repliesPath")
                    }
                }
        } catch (e: IOException) {
            throw IllegalStateException("Unable to close xml input stream", e)
        }
    }

    override fun getReply(key: String): Reply {
        return replies[key] ?: throw IllegalArgumentException("No reply for key: $key")
    }

}