package com.yenen.ahmet.location_service

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.os.Looper
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

class LocationService constructor(
    private val context: Context,
    private val isSingle: Boolean,
    private val interval:Long,
    private val fastestInterval:Long
) {

    private val client = LocationServices.getFusedLocationProviderClient(context)
    private var listener: LocationListener? = null
    private val locationCallback = LocationCallbackInner()

    fun setListener(listener: LocationListener) {
        this.listener = null
        this.listener = listener
    }

    fun unBind() {
        listener = null
    }

    fun startLocation() {
        val request = LocationRequest().setInterval(interval).setFastestInterval(fastestInterval)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        val requestBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(request).setAlwaysShow(true)

        val result = LocationServices.getSettingsClient(context)
            .checkLocationSettings(requestBuilder.build())

        result.addOnSuccessListener {
            client.requestLocationUpdates(request, locationCallback, Looper.myLooper())
        }

        result.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    val resolvable = it
                    resolvable.startResolutionForResult(
                        context as Activity,
                        11
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                } catch (ex: Exception) {

                }

            }
        }

        try {
            client.lastLocation.addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    listener?.onLocation(it.result!!)
                }
            }
        } catch (ex: Exception) {

        }

    }

    fun removeUpdate() {
        client.removeLocationUpdates(locationCallback)
    }

    inner class LocationCallbackInner : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            p0?.lastLocation?.let {
                listener?.onLocation(it)
                if (isSingle) {
                    removeUpdate()
                }
            }
        }
    }

}
