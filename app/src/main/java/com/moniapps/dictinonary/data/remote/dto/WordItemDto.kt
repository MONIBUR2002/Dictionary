package com.moniapps.dictinonary.data.remote.dto

data class WordItemDto(
    val word: String ? = null,
    val phonetic: String? = null,
    val phonetics: List<PhoneticDto>? = null,
    val meanings: List<MeaningDto>? = null
)