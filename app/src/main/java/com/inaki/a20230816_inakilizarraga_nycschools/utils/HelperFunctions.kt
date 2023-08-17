package com.inaki.a20230816_inakilizarraga_nycschools.utils

import retrofit2.Response

/**
 * This helper function makes the network call generic so it can be used by any component
 * Allowing us to have more scalability
 */
suspend fun <T> makeNetworkConnection(
    networkCheck: () -> Boolean,
    request: suspend () -> Response<T>,
    success: suspend (State.SUCCESS<T>) -> Unit,
    error: suspend (State.ERROR) -> Unit
) {
    try {
        if (networkCheck()) {
            val response = request.invoke()
            if (response.isSuccessful) {
                response.body()?.let {
                    success(State.SUCCESS(it))
                } ?: throw NullBodyException()
            } else {
                throw FailResponseException(response.errorBody()?.string())
            }
        } else {
            throw NoNetworkAvailable()
        }
    } catch (e: Exception) {
        error(State.ERROR(e))
    }
}