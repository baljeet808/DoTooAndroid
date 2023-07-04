package com.baljeet.youdotoo.di

import android.app.Application
import androidx.room.Room
import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesYouDoTooDatabase(app : Application) : YouDoTooDatabase{
        return Room.databaseBuilder(
            context = app,
            YouDoTooDatabase::class.java,
            "YouDoToo.db"
        ).build()
    }
}