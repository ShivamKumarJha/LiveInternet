package com.shivamkumarjha.liveinternet.view

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.shivamkumarjha.liveinternet.R
import com.shivamkumarjha.liveinternet.model.ConnectionState
import com.shivamkumarjha.liveinternet.viewmodel.ConnectionLiveData

class MainActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var cellTextView: TextView
    private lateinit var wifiTextView: TextView
    private lateinit var ethernetTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        statusTextView = findViewById(R.id.text_view_internet_status)
        cellTextView = findViewById(R.id.text_view_cell_status)
        wifiTextView = findViewById(R.id.text_view_wifi_status)
        ethernetTextView = findViewById(R.id.text_view_eth_status)
        observeConnection()
    }

    private fun observeConnection() {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val connectionLiveData = ConnectionLiveData(connectivityManager)
        connectionLiveData.observe(this,
            Observer<ConnectionState> {
                if (it.isCellular)
                    cellTextView.text = resources.getString(R.string.cell_available)
                else
                    cellTextView.text = resources.getString(R.string.cell_unavailable)

                if (it.isWiFi)
                    wifiTextView.text = resources.getString(R.string.wifi_available)
                else
                    wifiTextView.text = resources.getString(R.string.wifi_unavailable)

                if (it.isEthernet)
                    ethernetTextView.text = resources.getString(R.string.eth_available)
                else
                    ethernetTextView.text = resources.getString(R.string.eth_unavailable)

                if (it.isInternetAvailable)
                    statusTextView.text = resources.getString(R.string.internet_available)
                else
                    statusTextView.text = resources.getString(R.string.internet_unavailable)
            })
    }
}