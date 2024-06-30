package com.example.nfckey.di

import com.example.nfckey.ApplicationViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object MainApplicationComponent {

    @Provides
    @Singleton
    fun provideMainViewModel(): ApplicationViewModel = ApplicationViewModel()
}