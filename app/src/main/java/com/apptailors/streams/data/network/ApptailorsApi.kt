package com.apptailors.streams.data.network

import com.apptailors.streams.data.entity.BuffResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApptailorsApi {

    @GET("buffs/{id}")
    suspend fun getQuestion(@Path("id")id: Int): Response<BuffResult>
}