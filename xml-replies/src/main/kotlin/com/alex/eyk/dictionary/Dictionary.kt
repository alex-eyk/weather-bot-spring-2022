package com.alex.eyk.dictionary

data class Dictionary(
    val code: String,
    val languageLocalName: String,
    val default: Boolean,
    val replies: Map<String, Reply>,
    val words: Map<String, String>
)
