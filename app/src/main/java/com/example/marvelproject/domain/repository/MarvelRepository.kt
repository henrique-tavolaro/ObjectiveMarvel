package com.example.marvelproject.domain.repository

import com.example.marvelproject.domain.model.RetrofitResponse
import kotlinx.coroutines.flow.Flow

interface MarvelRepository {

    suspend fun searchCharacter(
        name: String,
        limit: Int,
        offset: Int,
        ts: String,
        apikey: String,
        hash: String
    ) : Flow<Resource<RetrofitResponse?>>

}