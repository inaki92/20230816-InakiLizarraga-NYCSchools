package com.inaki.a20230816_inakilizarraga_nycschools.model.scores


import com.google.gson.annotations.SerializedName

data class Score(
    @SerializedName("dbn")
    val dbn: String? = null,
    @SerializedName("num_of_sat_test_takers")
    val numOfSatTestTakers: String? = null,
    @SerializedName("sat_critical_reading_avg_score")
    val satCriticalReadingAvgScore: String? = null,
    @SerializedName("sat_math_avg_score")
    val satMathAvgScore: String? = null,
    @SerializedName("sat_writing_avg_score")
    val satWritingAvgScore: String? = null,
    @SerializedName("school_name")
    val schoolName: String? = null
)