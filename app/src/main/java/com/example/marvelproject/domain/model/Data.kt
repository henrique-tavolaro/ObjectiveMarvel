package com.example.marvelproject.model

import com.example.marvelproject.domain.model.Result

data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Result>,
    val total: Int
)