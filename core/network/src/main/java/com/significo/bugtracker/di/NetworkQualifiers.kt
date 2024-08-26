package com.significo.bugtracker.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppBaseUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthToken

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TokenInterceptor
