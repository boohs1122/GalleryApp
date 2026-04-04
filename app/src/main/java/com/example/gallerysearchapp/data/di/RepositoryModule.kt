package com.example.gallerysearchapp.data.di;

import com.example.gallerysearchapp.data.repository.ImageRepository
import com.example.gallerysearchapp.data.repository.ImageRepositoryImpl
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
        imageRepositoryImpl: ImageRepositoryImpl,
    ): ImageRepository
}
