package com.alex.eyk.xml.impl

import org.xml.sax.Attributes


private const val ATTR_KEY = "key"
private const val ATTR_FORMAT = "format"
private const val ATTR_MARKDOWN = "markdown"

fun Attributes.getKey(): String {
    val key = this.getValue(ATTR_KEY)
    if (key != null) {
        return key
    } else {
        throw IllegalStateException("Attribute `key` should be define for all replies")
    }
}

fun Attributes.getFormat(default: Boolean): Boolean {
    val format = this.getValue(ATTR_FORMAT) ?: return default
    try {
        return format.toBooleanStrict()
    } catch (e: IllegalArgumentException) {
        throw IllegalStateException(
            "Illegal value for param `format` (available only `true` or `false`)"
        )
    }
}

fun Attributes.getMarkdown(default: Boolean): Boolean {
    val markdown = this.getValue(ATTR_MARKDOWN) ?: return default
    try {
        return markdown.toBooleanStrict()
    } catch (e: IllegalArgumentException) {
        throw IllegalStateException(
            "Illegal value for param `markdown` (available only `true` or `false`)"
        )
    }
}
