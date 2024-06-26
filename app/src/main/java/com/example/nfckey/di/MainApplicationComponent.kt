package com.example.nfckey.di

import com.example.nfckey.ApplicationViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object MainApplicationComponent {

    @Provides
    fun provideMainViewModel(): ApplicationViewModel = ApplicationViewModel()
}