package com.alex.eyk.bot.weather.core.entity.reply.impl

import com.alex.eyk.bot.weather.core.util.ResourceUtils
import com.alex.eyk.dictionary.provider.AbstractDictionaryProvider
import com.alex.eyk.processor.DictionaryProvider
import org.springframework.stereotype.Service

const val DICTIONARY_DIRECTORY = "dictionary/"

@DictionaryProvider
@Service
class DictionaryProviderImpl : AbstractDictionaryProvider(
    ResourceUtils.scanResourceDirectory(DICTIONARY_DIRECTORY)
)