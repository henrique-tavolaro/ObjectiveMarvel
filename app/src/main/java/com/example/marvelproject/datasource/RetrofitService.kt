package com.example.marvelproject.datasource

import com.example.marvelproject.domain.model.RetrofitResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("characters")
    suspend fun search(
        @Query("nameStartsWith") name: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("ts") timestamp: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String

    ) : Response<RetrofitResponse>
}