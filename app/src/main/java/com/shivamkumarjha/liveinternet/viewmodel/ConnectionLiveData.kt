package com.shivamkumarjha.liveinternet.viewmodel

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData

class ConnectionLiveData internal constructor(private val connectivityManager: ConnectivityManager) :
    LiveData<Boolean>() {

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            postValue(true)
        }

        override fun onLost(network: Network) {
            postValue(false)
        }
    }

    override fun onActive() {
        super.onActive()

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
                    postValue(true)
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->
                    postValue(true)
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->
                    postValue(true)
                else -> postValue(false)
            }
        }
        postValue(false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
