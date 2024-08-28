package com.moniapps.dictinonary.di

import com.moniapps.dictinonary.data.remote.repository.DictionaryRepositoryImpl
import com.moniapps.dictinonary.domain.repository.DictionaryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindDictionaryRepository(
        dictionaryRepository: DictionaryRepositoryImpl
    ):DictionaryRepository
}