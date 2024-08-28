package com.moniapps.dictinonary.presentation

import com.moniapps.dictinonary.domain.model.Phonetic
import com.moniapps.dictinonary.domain.model.WordItem

data class MainState(
    val isLoading: Boolean = false,
    val searchWord: String = "",
    val wordItem: WordItem? = null,
    val phonetic: Phonetic? = null
)
