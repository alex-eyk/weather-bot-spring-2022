package com.alex.eyk.bot.processor

import com.ximand.properties.PropertiesPath
import com.ximand.properties.Property

@PropertiesPath("res:/replies-processor.properties")
class ProcessorProperties {

    @Property(name = "replies.reply_entity_package", defaultValue = "")
    lateinit var replyEntityPackage: String

    @Property(name = "replies.absolute_path", defaultValue = "")
    lateinit var repliesAbsolutePath: String

    @Property(name = "generated.absolute_path", defaultValue = "")
    lateinit var generatedObjectAbsolutePath: String

    @Property(name = "generated.file_name", defaultValue = "Replies")
    lateinit var generatedFileName: String

}