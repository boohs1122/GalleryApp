package com.example.gallerysearchapp.data.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.gallerysearchapp.data.connectivity.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext context: Context,
    ): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(
        connectivityManager: ConnectivityManager,
    ): NetworkConnectivityObserver {
        return NetworkConnectivityObserver(connectivityManager)
    }
}
