package com.significo.bugtracker.di

import com.significo.bugtracker.api.GITHUB_BASE_URL
import com.significo.bugtracker.api.GitHubApi
import com.significo.bugtracker.core.network.BuildConfig
import com.significo.bugtracker.interceptors.NetworkTokenInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Duration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @AppBaseUrl
    @Provides
    @Singleton
    fun provideAppBaseUrl(): HttpUrl = GITHUB_BASE_URL.toHttpUrl()

    @AuthToken
    @Provides
    @Singleton
    fun provideAuthToken(): String = BuildConfig.GITHUB_PAT

    @TokenInterceptor
    @Provides
    @Singleton
    fun provideTokenInterceptor(
        @AppBaseUrl baseUrl: HttpUrl,
        @AuthToken authToken: String
    ): Interceptor = NetworkTokenInterceptor(baseUrl, authToken)

    @Provides
    @Singleton
    fun provideClient() = OkHttpClient.Builder()
        .connectTimeout(Duration.ofSeconds(15))
        .readTimeout(Duration.ofSeconds(30))
        .writeTimeout(Duration.ofSeconds(30))
        .protocols(listOf(Protocol.HTTP_1_1))
        .build()

    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory {
        val moshi = Moshi.Builder()
            .build()

        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: dagger.Lazy<OkHttpClient>,
        @AppBaseUrl baseUrl: HttpUrl,
        @TokenInterceptor tokenInterceptor: Interceptor,
        converterFactory: Converter.Factory
    ): Retrofit {
        val client = okHttpClient.get().newBuilder()
            .addLoggingInterceptor()
            .addInterceptor(tokenInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideGitHubApi(retrofit: Retrofit): GitHubApi = retrofit.create(GitHubApi::class.java)

    private fun OkHttpClient.Builder.addLoggingInterceptor() = apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
        }
    }
}
