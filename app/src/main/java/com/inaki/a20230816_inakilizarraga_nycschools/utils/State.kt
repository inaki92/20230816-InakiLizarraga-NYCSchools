package com.inaki.a20230816_inakilizarraga_nycschools.utils

sealed class State<out T> {
    object LOADING : State<Nothing>()
    data class SUCCESS<T>(val result: T) : State<T>()
    data class ERROR(val error: Exception) : State<Nothing>()
}
