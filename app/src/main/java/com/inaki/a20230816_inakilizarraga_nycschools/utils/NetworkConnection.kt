package com.inaki.a20230816_inakilizarraga_nycschools.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

class NetworkConnection @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {

    fun isNetworkAvailable(): Boolean =
        connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        } ?: false
}