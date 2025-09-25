package com.example.erp_qr.di

import android.content.Context
import android.content.SharedPreferences
import com.example.erp_qr.retrofit.NetworkService
import com.example.erp_qr.retrofit.RetrofitProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            "erp_qr",
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideRetrofitService(): NetworkService {
        return RetrofitProvider.networkService
    }
}