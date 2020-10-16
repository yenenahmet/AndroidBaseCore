package com.yenen.ahmet.location_service

import android.content.Context
import android.location.Location
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

abstract class BackgroundLocationService constructor(
    private val context: Context,
    workerParameters: WorkerParameters

) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        return try {
            val locationService = LocationService(context, true, 0, 0)
            locationService.setListener(object:LocationListener{
                override fun onLocation(location: Location) {
                    locationService.unBind()
                    onDoWork(location,context)
                }
            })
            locationService.startLocation()

            Result.success()
        } catch (ex: Exception) {
            Result.failure()
        }
    }


    companion object {
        fun initWorkManager(
            context: Context
        ): UUID {
            val periodicWork =
                PeriodicWorkRequest.Builder(
                    BackgroundLocationService::class.java,
                    15L,
                    TimeUnit.MINUTES
                ).addTag("LocationUpdate").build()



         WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "Location",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    periodicWork
                )

            return periodicWork.id
        }
    }


    protected abstract fun onDoWork(location:Location,context: Context)
}