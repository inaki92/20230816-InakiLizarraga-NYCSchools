package com.inaki.a20230816_inakilizarraga_nycschools.rest

import com.inaki.a20230816_inakilizarraga_nycschools.model.schools.School
import com.inaki.a20230816_inakilizarraga_nycschools.model.scores.Score
import retrofit2.Response
import javax.inject.Inject

interface MainRepository {
    suspend fun retrieveSchools(): Response<List<School>>
    suspend fun retrieveScores(): Response<List<Score>>
}

class MainRepositoryImpl @Inject constructor(
    private val api: ServiceApi
) : MainRepository {
    override suspend fun retrieveSchools(): Response<List<School>> = api.getSchools()
    override suspend fun retrieveScores(): Response<List<Score>> = api.getScores()
}