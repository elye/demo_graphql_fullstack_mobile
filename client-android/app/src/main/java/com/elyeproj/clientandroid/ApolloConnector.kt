package com.elyeproj.clientandroid

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient


object ApolloConnector {
    private val BASE_URL = "http://EDIT_YOUR_HOST_HERE:4000"
    fun setupApollo(): ApolloClient {
        val okHttpClient = OkHttpClient.Builder().build()
        return ApolloClient.builder().serverUrl(BASE_URL).okHttpClient(okHttpClient).build()
    }
}
