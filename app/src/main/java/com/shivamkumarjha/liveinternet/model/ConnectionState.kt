package com.shivamkumarjha.liveinternet.model

data class ConnectionState(
    val isCellular: Boolean = false,
    val isWiFi: Boolean = false,
    val isEthernet: Boolean = false,
    val isInternetAvailable: Boolean = false
)