package com.elyeproj.clientandroid

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient


object ApolloConnector {
    private const val BASE_URL = "${MainActivity.SCHEME}://${MainActivity.BASE}:${MainActivity.PORT}"
    fun setupApollo(): ApolloClient {
        val okHttpClient = OkHttpClient.Builder().build()
        return ApolloClient.builder().serverUrl(BASE_URL).okHttpClient(okHttpClient).build()
    }
}
