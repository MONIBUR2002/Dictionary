package com.moniapps.dictinonary.domain.repository


import com.moniapps.dictinonary.domain.model.WordItem
import com.moniapps.dictinonary.util.Result
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    suspend fun getWordResult(
        word: String
    ):Flow<Result<WordItem>>
}