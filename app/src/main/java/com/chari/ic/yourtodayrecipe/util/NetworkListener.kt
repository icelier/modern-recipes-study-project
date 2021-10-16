package com.chari.ic.yourtodayrecipe.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val TAG = "NetworkListener"
class NetworkListener(): ConnectivityManager.NetworkCallback() {
    private val isNetworkAvailable = MutableStateFlow(false)

    fun checkNetworkAvailability(context: Context): StateFlow<Boolean> {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(this)
        } else {
            // could filter using .addCapability(int) or .addTransportType(int) on Builder
//            val networkChangeFilter = NetworkRequest.Builder().build()
            connectivityManager.registerNetworkCallback(
                NetworkRequest.Builder().build(),
                this
            )
        }

        var isConnected = false

        connectivityManager.allNetworks.forEach { network ->
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            if (capabilities != null) {
                if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    isConnected = true
                    return@forEach
                }
            }
        }

        isNetworkAvailable.value = isConnected

        return isNetworkAvailable
    }

    override fun onAvailable(network: Network) {
        Log.d(TAG, "Network OnAvailable")
        isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        Log.d(TAG, "Network onLost")
        isNetworkAvailable.value = false
    }
}