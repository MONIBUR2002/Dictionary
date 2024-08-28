package com.moniapps.dictinonary.data.remote.api

import com.moniapps.dictinonary.data.remote.dto.WordResultDto
import retrofit2.http.GET
import retrofit2.http.Path


fun interface DictionaryApi {
    @GET("{word}")
    suspend fun getWordResult(
        @Path("word") word : String
    ): WordResultDto?
    companion object{
        const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"
    }
}