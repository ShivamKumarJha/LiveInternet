package com.shivamkumarjha.liveinternet.viewmodel

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import com.shivamkumarjha.liveinternet.model.ConnectionState

class ConnectionLiveData(private val connectivityManager: ConnectivityManager) :
    LiveData<ConnectionState>() {

    var cellular: Boolean = false
    var wiFi: Boolean = false
    var ethernet: Boolean = false
    var internet: Boolean = false

    private fun pushStatus() {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            cellular = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            wiFi = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            ethernet = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            internet = cellular || wiFi || ethernet
            Log.d("ConnectivityManager", "$cellular $wiFi $ethernet $internet")
        }
        postValue(
            ConnectionState(
                isCellular = cellular,
                isWiFi = wiFi,
                isEthernet = ethernet,
                isInternetAvailable = internet
            )
        )
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            internet = true
            pushStatus()
        }

        override fun onLost(network: Network) {
            cellular = false
            wiFi = false
            ethernet = false
            internet = false
            pushStatus()
        }
    }

    override fun onActive() {
        super.onActive()
        pushStatus()

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
