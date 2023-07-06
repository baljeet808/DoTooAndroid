package com.baljeet.youdotoo.di

import android.app.Application
import androidx.room.Room
import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import com.baljeet.youdotoo.data.repository_implementations.ProjectRepositoryImpl
import com.baljeet.youdotoo.domain.repository_interfaces.ProjectRepository
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
    fun providesProjectRepository(localDB : YouDoTooDatabase): ProjectRepository {
        return ProjectRepositoryImpl(localDB)
    }

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