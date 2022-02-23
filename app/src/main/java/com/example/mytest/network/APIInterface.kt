package com.example.mytest.network

import com.example.mytest.models.TrendingModel
import retrofit2.http.GET

interface APIInterface {
    @GET("search/repositories?sort=stars&q=since:>daily")
    suspend fun getTrendingRepo(): TrendingModel
}