package com.moniapps.dictinonary.data.remote.mapper

import com.moniapps.dictinonary.data.remote.dto.DefinitionDto
import com.moniapps.dictinonary.data.remote.dto.MeaningDto
import com.moniapps.dictinonary.data.remote.dto.WordItemDto
import com.moniapps.dictinonary.domain.model.Definition
import com.moniapps.dictinonary.domain.model.Meaning
import com.moniapps.dictinonary.domain.model.WordItem

fun WordItemDto.toWordItem() = WordItem(
    word = word ?: "",
    meanings = meanings?.map {
        it.toMeaning()
    } ?: emptyList(),
    phonetic = phonetic ?: ""
)


fun MeaningDto.toMeaning() = Meaning(
    definition = DefinitionDtotoDefinition(definitions?.get(0)),
    partOfSpeech = partOfSpeech ?: ""
)

fun DefinitionDtotoDefinition(
    definitions: DefinitionDto?
) = Definition(
    definition = definitions?.definition ?: "",
    example = definitions?.example ?: "",
)
