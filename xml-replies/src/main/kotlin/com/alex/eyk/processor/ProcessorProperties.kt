package com.alex.eyk.processor

import com.ximand.properties.PropertiesPath
import com.ximand.properties.Property

@PropertiesPath("res:/replies-processor.properties")
class ProcessorProperties {

    @Property(name = "replies.absolute_path", defaultValue = "")
    lateinit var repliesAbsolutePath: String

    @Property(name = "generated.file_name", defaultValue = "Replies")
    lateinit var generatedFileName: String

}