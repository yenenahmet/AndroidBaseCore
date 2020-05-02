package com.yenen.ahmet.basecorelibrary.base.utility

import android.annotation.SuppressLint
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.lang.StringBuilder


@SuppressLint("SimpleDateFormat")
object DateUtils {
    val monthNames =
        arrayOf("Oca", "Şub", "Mar", "Nis", "May", "Haz", "Tem", "Ağu", "Eyl", "Eki", "Kas", "Ara")

    val dfServer = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    var showFormat = "dd.MM.yyyy HH:mm"

    fun getServerDateTimeFormatShort(dateTime: String): String {
        try {
            val simpleDateFormat = SimpleDateFormat(dfServer)
            val date = simpleDateFormat.parse(dateTime)
            val cal = Calendar.getInstance()
            cal.time = date!!
            val day = cal.get(Calendar.DAY_OF_MONTH).toString()
            val mont = monthNames[cal.get(Calendar.MONTH)]
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val h  =if(hour>9){
                hour.toString()
            }else{
                "0${hour}"
            }
            val minute = cal.get(Calendar.MINUTE)
            val m = if(minute>9){
                minute.toString()
            }else{
                "0${minute}"
            }
            val time = "${h}:${m}"
            return "$day $mont $time"
        } catch (ex: Exception) {
            return dateTime
        }
    }


    fun compareToDay(dateTime: String):Int{
        try{
            val simpleDateFormat = SimpleDateFormat(dfServer)
            val date = simpleDateFormat.parse(dateTime)
            val now = Date()
            return now.compareTo(date)
        }catch (ex:Exception){
            return -2
        }
    }


    fun getNowDate():String{
        val dateFormat = SimpleDateFormat(dfServer)
        return dateFormat.format(Date())
    }


    fun getDateTimeShowFormat(date:String):String{
        try{
            val dateFormat = SimpleDateFormat(dfServer)
            val showFormat = SimpleDateFormat(showFormat)
            dateFormat.parse(date)?.let {
               return showFormat.format(it)
            }

        }catch (ex:Exception){

        }

        return ""
    }

    fun getBetweenTheTwoTimes(date1:String,date2:String,format:String):String{
        try {
            val date1Format = SimpleDateFormat(format)
            val date1Date = date1Format.parse(date1)

            val date2Format = SimpleDateFormat(format)
            val date2Date = date1Format.parse(date2)

            val diff = date1Date.time - date2Date.time

            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            val sb = StringBuilder()

            if(days>0){
                sb.append("${days} gün ")
            }

            if(hours>0){
                sb.append("${hours} saat ")
            }

            if(minutes>0){
                sb.append("${minutes} dakika ")
            }

            return sb.toString()

        }catch (ex:Exception){
            return ""
        }
    }
}