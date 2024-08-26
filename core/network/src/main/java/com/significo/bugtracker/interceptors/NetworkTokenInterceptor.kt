package com.significo.bugtracker.interceptors

import com.significo.bugtracker.di.TokenInterceptor
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

@TokenInterceptor
class NetworkTokenInterceptor(
    private val baseUrl: HttpUrl,
    private val token: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (baseUrl.host.contains(request.url.host)) {
            val builder = chain.request().newBuilder().apply {
                header(
                    name = "Authorization",
                    value = "token $token"
                )
                header(
                    name = "Accept",
                    value = "application/vnd.github.v3+json"
                )
            }

            return chain.proceed(builder.build())
        }

        return chain.proceed(request)
    }
}
