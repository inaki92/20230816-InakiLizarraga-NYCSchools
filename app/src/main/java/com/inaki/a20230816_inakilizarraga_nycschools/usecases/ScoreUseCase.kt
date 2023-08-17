package com.inaki.a20230816_inakilizarraga_nycschools.usecases

import com.inaki.a20230816_inakilizarraga_nycschools.model.scores.Score
import com.inaki.a20230816_inakilizarraga_nycschools.rest.MainRepository
import com.inaki.a20230816_inakilizarraga_nycschools.utils.NetworkConnection
import com.inaki.a20230816_inakilizarraga_nycschools.utils.State
import com.inaki.a20230816_inakilizarraga_nycschools.utils.makeNetworkConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ScoreUseCase @Inject constructor(
    private val repository: MainRepository,
    private val networkConnection: NetworkConnection
) {

    operator fun invoke(): Flow<State<List<Score>>> = flow {
        makeNetworkConnection(
            networkCheck = { networkConnection.isNetworkAvailable() },
            request = { repository.retrieveScores() },
            success = { emit(it) },
            error = { emit(it) }
        )
    }
}