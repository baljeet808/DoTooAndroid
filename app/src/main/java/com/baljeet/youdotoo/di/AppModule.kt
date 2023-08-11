package com.baljeet.youdotoo.di

import android.app.Application
import androidx.room.Room
import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import com.baljeet.youdotoo.data.repository_implementations.ColorPaletteRepositoryImpl
import com.baljeet.youdotoo.data.repository_implementations.DatabaseOperationsRepositoryImpl
import com.baljeet.youdotoo.data.repository_implementations.DoTooItemsRepositoryImpl
import com.baljeet.youdotoo.data.repository_implementations.InvitationsRepositoryImpl
import com.baljeet.youdotoo.data.repository_implementations.MessageRepositoryImpl
import com.baljeet.youdotoo.data.repository_implementations.NotificationRepositoryImpl
import com.baljeet.youdotoo.data.repository_implementations.ProjectRepositoryImpl
import com.baljeet.youdotoo.data.repository_implementations.UserRepositoryImpl
import com.baljeet.youdotoo.domain.repository_interfaces.ColorPaletteRepository
import com.baljeet.youdotoo.domain.repository_interfaces.DatabaseOperationsRepository
import com.baljeet.youdotoo.domain.repository_interfaces.DoTooItemsRepository
import com.baljeet.youdotoo.domain.repository_interfaces.InvitationsRepository
import com.baljeet.youdotoo.domain.repository_interfaces.MessageRepository
import com.baljeet.youdotoo.domain.repository_interfaces.NotificationRepository
import com.baljeet.youdotoo.domain.repository_interfaces.ProjectRepository
import com.baljeet.youdotoo.domain.repository_interfaces.UserRepository
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
    fun providesDoTooItemRepository(localDB : YouDoTooDatabase): DoTooItemsRepository {
        return DoTooItemsRepositoryImpl(localDB)
    }

    @Provides
    @Singleton
    fun providesUserRepository(localDB : YouDoTooDatabase): UserRepository {
        return UserRepositoryImpl(localDB)
    }
    @Provides
    @Singleton
    fun providesMessageRepository(localDB : YouDoTooDatabase): MessageRepository {
        return MessageRepositoryImpl(localDB)
    }

    @Provides
    @Singleton
    fun providesColorPaletteRepository(localDB : YouDoTooDatabase): ColorPaletteRepository {
        return ColorPaletteRepositoryImpl(localDB)
    }

    @Provides
    @Singleton
    fun providesInvitationRepository(localDB : YouDoTooDatabase): InvitationsRepository {
        return InvitationsRepositoryImpl(localDB)
    }
    @Provides
    @Singleton
    fun providesDatabaseOperationsRepository(localDB : YouDoTooDatabase): DatabaseOperationsRepository {
        return DatabaseOperationsRepositoryImpl(localDB)
    }
    @Provides
    @Singleton
    fun providesNotificationsRepository(localDB : YouDoTooDatabase): NotificationRepository {
        return NotificationRepositoryImpl(localDB)
    }

    @Provides
    @Singleton
    fun providesYouDoTooDatabase(app : Application) : YouDoTooDatabase{
        return Room.databaseBuilder(
            context = app,
            YouDoTooDatabase::class.java,
            "YouDoToo.db"
        ).fallbackToDestructiveMigration().build()
    }
}