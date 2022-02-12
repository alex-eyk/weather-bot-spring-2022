package com.alex.eyk.xml.attrs

import org.xml.sax.Attributes

private const val QUERY_ATTR = "q"
private const val NAME_ATTR = "name"

object AttributesArgsExt {

    fun Attributes.getArgumentQuery(): String {
        return getString(QUERY_ATTR)
    }

    fun Attributes.getArgumentName(): String {
        return getString(NAME_ATTR)
    }

}