package com.example.marvelproject.repository

import com.example.marvelproject.datasource.RetrofitService
import com.example.marvelproject.domain.model.RetrofitResponse
import com.example.marvelproject.domain.repository.MarvelRepository
import com.example.marvelproject.domain.repository.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MarvelRepositoryImpl (
    private val retrofitService: RetrofitService
) : MarvelRepository {

    override suspend fun searchCharacter(
        name: String,
        limit: Int,
        offset: Int,
        ts: String,
        apikey: String,
        hash: String
    ): Flow<Resource<RetrofitResponse?>> = flow {
        emit(Resource.loading(null))

        try {
            val response = retrofitService.search(name, limit, offset, ts, apikey, hash)
            emit(Resource.success(response.body()))
        } catch (e: Exception) {
            emit(Resource.error<RetrofitResponse>(e.message ?: "Unknown Error", null))
        }

    }
}