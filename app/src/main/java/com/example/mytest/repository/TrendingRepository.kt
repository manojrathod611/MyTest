package com.example.mytest.repository

import com.example.mytest.base.BaseRepository
import com.example.mytest.network.APIInterface

class TrendingRepository(
    private val api: APIInterface
) : BaseRepository() {

    suspend fun getTrendingRepo() = safeApiCall {
        api.getTrendingRepo()
    }

}