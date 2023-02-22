package com.example.diaryandroid.di

import android.content.Context
import androidx.room.Room
import com.example.util.connectivity.NetworkConnectivityObserver
import com.example.mongo.database.ImagesDatabase
import com.example.util.Constants.IMAGES_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context
    ): ImagesDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ImagesDatabase::class.java,
            name = IMAGES_DATABASE
        ).build()
    }

    @Provides
    @Singleton
    fun providesFirstDao(database: ImagesDatabase) = database.imagesToUploadDao()

    @Provides
    @Singleton
    fun providesSecondDao(database: ImagesDatabase) = database.imageToDeleteDao()

    @Singleton
    @Provides
    fun provideNetworkConnectivityObserver(
        @ApplicationContext context: Context
    ) = NetworkConnectivityObserver(context = context)
}