package com.moniapps.dictinonary.data.remote.repository

import android.app.Application
import com.moniapps.dictinonary.R
import com.moniapps.dictinonary.data.remote.api.DictionaryApi
import com.moniapps.dictinonary.data.remote.mapper.toWordItem
import com.moniapps.dictinonary.domain.model.WordItem
import com.moniapps.dictinonary.domain.repository.DictionaryRepository
import com.moniapps.dictinonary.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(
    private val dictionaryApi: DictionaryApi,
    private val application: Application
):DictionaryRepository {
    override suspend fun getWordResult(
        word: String
    ): Flow<Result<WordItem>> =
        flow {
            emit(Result.Loading(true))
            val remoteWordResultDto = try {
                dictionaryApi.getWordResult(word)
            } catch (e: HttpException){
                e.printStackTrace()
                emit(Result.Error(application.getString(R.string.can_t_get_word_result)))
                emit(Result.Loading(false))
                return@flow
            }catch (e: IOException){
                e.printStackTrace()
                emit(Result.Error(application.getString(R.string.can_t_get_word_result)))
                emit(Result.Loading(false))
                return@flow
            }catch (e: Exception){
                e.printStackTrace()
                emit(Result.Error(application.getString(R.string.can_t_get_word_result)))
                emit(Result.Loading(false))
                return@flow
            }
            remoteWordResultDto?.let{ wordResultDto ->
                wordResultDto[0]?.let { wordItemDto ->
                    emit(Result.Success(wordItemDto.toWordItem()))
                    emit(Result.Loading(false))
                    return@flow
                }

            }
            emit(Result.Error(application.getString(R.string.can_t_get_word_result)))
            emit(Result.Loading(false))

        }
}