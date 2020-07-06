package com.shivamkumarjha.liveinternet.view

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.shivamkumarjha.liveinternet.R
import com.shivamkumarjha.liveinternet.viewmodel.ConnectionLiveData

class MainActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        statusTextView = findViewById(R.id.text_view_status)
        observeConnection()
    }

    private fun observeConnection() {
        val connectivityManager =
                this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val connectionLiveData = ConnectionLiveData(connectivityManager)
        connectionLiveData.observe(this,
                Observer<Boolean> {
                    if (it) {
                        statusTextView.text = resources.getString(R.string.internet_available)
                    } else {
                        statusTextView.text = resources.getString(R.string.internet_unavailable)
                    }
                })
    }
}