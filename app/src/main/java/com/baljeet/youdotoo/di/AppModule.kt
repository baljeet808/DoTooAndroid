package com.baljeet.youdotoo.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Updated by Baljeet singh.
 * **/
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //nothing is here for now, it was setup for setting data layer but then figured
    //That is overkill in this project scope
    //Now this is just here for future cases
    //Might delete this if no use found
}