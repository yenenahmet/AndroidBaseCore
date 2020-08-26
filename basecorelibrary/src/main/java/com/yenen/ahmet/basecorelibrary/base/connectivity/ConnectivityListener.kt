package com.yenen.ahmet.basecorelibrary.base.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


class ConnectivityListener : BroadcastReceiver() {

    private var listener: ConnectivityChangeListener? = null


    override fun onReceive(context: Context?, intent: Intent?) {
        var isWifiConn: Boolean = false
        var isMobileConn: Boolean = false
        var isEthernet: Boolean = false

        context?.let {
            val cm = it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            cm.activeNetwork?.let { nw ->
                cm.getNetworkCapabilities(nw)?.let { nc ->
                    if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        isWifiConn = true
                    }
                    if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        isMobileConn = true
                    }
                    if (nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        isEthernet = true
                    }
                }

            }
            listener?.onChange(isWifiConn, isMobileConn, isEthernet)

        }
    }


    fun setListener(listener: ConnectivityChangeListener) {
        this.listener = null
        this.listener = listener
    }

    interface ConnectivityChangeListener {
        fun onChange(isWifiConn: Boolean, isMobileConn: Boolean, isEthernet: Boolean)
    }

}