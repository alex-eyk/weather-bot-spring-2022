package com.alex.eyk.processor

import com.ximand.properties.PropertiesPath
import com.ximand.properties.Property

@PropertiesPath("res:/replies-processor.properties")
class ProcessorProperties {

    @Property(name = "dictionary.directory_path", defaultValue = "")
    lateinit var dictionariesDirectoryPath: String

    @Property(name = "generated.replies.filename", defaultValue = "Replies")
    lateinit var generatedReplyFilename: String

    @Property(name = "generated.words.filename", defaultValue = "Words")
    lateinit var generatedWordFilename: String

}