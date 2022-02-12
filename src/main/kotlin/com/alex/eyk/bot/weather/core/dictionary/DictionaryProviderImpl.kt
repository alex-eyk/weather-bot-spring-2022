package com.alex.eyk.bot.weather.core.dictionary

import com.alex.eyk.bot.weather.core.util.ResourceUtils
import com.alex.eyk.dictionary.provider.AbstractDictionaryProvider
import org.springframework.stereotype.Service

const val DICTIONARY_DIRECTORY = "dictionary/"

@Service
class DictionaryProviderImpl : AbstractDictionaryProvider(
    ResourceUtils.scanResourceDirectory(DICTIONARY_DIRECTORY)
)