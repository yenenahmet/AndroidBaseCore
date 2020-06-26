package com.yenen.ahmet.location_service

import android.location.Location

interface LocationListener {
    fun onLocation(location:Location)
}