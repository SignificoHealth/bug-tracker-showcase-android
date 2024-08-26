package com.significo.bugtracker.logger

import android.util.Log
import com.significo.bugtracker.core.logger.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {

    @Provides
    @Singleton
    fun provideLogger() = object : Logger {
        override fun log(
            error: Throwable,
            tag: String?,
            msg: String?
        ) {
            if (BuildConfig.DEBUG) {
                Log.e(tag, msg, error)
            } else {
                // Send logs to a monitoring tool like Crashlytics, Sentry, etc.
            }
        }
    }
}
