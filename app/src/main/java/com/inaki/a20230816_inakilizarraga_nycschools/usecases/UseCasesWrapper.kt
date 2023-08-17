package com.inaki.a20230816_inakilizarraga_nycschools.usecases

import javax.inject.Inject

data class UseCasesWrapper @Inject constructor(
    val schoolUseCase: SchoolUseCase,
    val scoreUseCase: ScoreUseCase
)
