package com.moniapps.dictinonary.presentation

sealed class MainUIEvents {
    data class OnSearchWordChange(
        val newWord: String
    ) : MainUIEvents()
    object OnSearchClicked : MainUIEvents()
}