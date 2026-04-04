package com.example.gallerysearchapp.data.di;

import com.example.gallerysearchapp.data.repository.SearchRepository
import com.example.gallerysearchapp.data.repository.SearchRepositoryImpl
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
    abstract fun bindImageRepository(
        imageRepositoryImpl: SearchRepositoryImpl,
    ): SearchRepository
}
