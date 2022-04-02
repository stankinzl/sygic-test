package com.kinzlstanislav.sigyctest.core.helpers

import android.content.Context
import android.net.ConnectivityManager
import org.koin.core.annotation.Factory

@Factory
class NetworkHelper(private val context: Context) {
    fun isDeviceConnectedToInternet(): Boolean {
        var isConnectedToWifi = false
        var isConnectedToData = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager? ?: return false
        val netInfo = connectivityManager.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals("WIFI", ignoreCase = true)) if (ni.isConnected) isConnectedToWifi = true
            if (ni.typeName.equals("MOBILE", ignoreCase = true)) if (ni.isConnected) isConnectedToData = true
        }
        return isConnectedToWifi || isConnectedToData
    }
}