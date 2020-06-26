package com.yenen.ahmet.location_service

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

class BackgroundLocationService constructor(
    private val context: Context,
    workerParameters: WorkerParameters

) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        return try {
            val locationService = LocationService(context, true, 0, 0)
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

}