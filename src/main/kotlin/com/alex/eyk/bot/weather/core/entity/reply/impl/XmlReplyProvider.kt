package com.alex.eyk.bot.weather.core.entity.reply.impl

import com.alex.eyk.bot.weather.core.entity.reply.Reply
import com.alex.eyk.bot.weather.core.entity.reply.ReplyProvider
import com.alex.eyk.bot.weather.core.xml.impl.ReplyXmlParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException

const val REPLIES_PATH = "replies.xml"

@Service
class XmlReplyProvider @Autowired constructor(
    xmlRepliesParser: ReplyXmlParser
) : ReplyProvider {

    private val replies: Map<String, Reply>

    init {
        try {
            javaClass.classLoader.getResourceAsStream(REPLIES_PATH)
                .use { inputStream ->
                    if (inputStream != null) {
                        this.replies = xmlRepliesParser.parse(inputStream)
                    } else {
                        throw IllegalStateException(
                            "File with replies not found, path: resources/$REPLIES_PATH"
                        )
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