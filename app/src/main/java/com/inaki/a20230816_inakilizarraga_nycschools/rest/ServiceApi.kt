package com.inaki.a20230816_inakilizarraga_nycschools.rest

import com.inaki.a20230816_inakilizarraga_nycschools.model.schools.School
import com.inaki.a20230816_inakilizarraga_nycschools.model.scores.Score
import com.inaki.a20230816_inakilizarraga_nycschools.utils.SCHOOLS_PATH
import com.inaki.a20230816_inakilizarraga_nycschools.utils.SCORE_PATH
import retrofit2.Response
import retrofit2.http.GET

interface ServiceApi {

    @GET(SCHOOLS_PATH)
    suspend fun getSchools(): Response<List<School>>

    @GET(SCORE_PATH)
    suspend fun getScores(): Response<List<Score>>
}