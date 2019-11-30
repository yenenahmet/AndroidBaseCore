package com.yenen.ahmet.basecorelibrary.base.local

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Build.VERSION_CODES.N
import android.os.LocaleList
import java.util.*

class LocaleManager constructor(private val sharedPreferencesHelper: SharedPreferencesHelper) {

    fun setLocale(c: Context): Context {
        return updateResources(c, sharedPreferencesHelper.getLanguage())
    }

    fun setNewLocale(c: Context, language: String): Context {
        sharedPreferencesHelper.addLanguage(language)
        return updateResources(c, sharedPreferencesHelper.getLanguage())
    }


    fun getLocale(): String {
        return sharedPreferencesHelper.getLanguage()
    }


    private fun updateResources(context: Context, language: String): Context {
        val newLocale = Locale(language)
        val res = context.resources
        val dm = res.displayMetrics
        val config = Configuration(res.configuration)


        if (Build.VERSION.SDK_INT >= N) {
            config.setLocale(newLocale)
            val localeList = LocaleList(newLocale)
            LocaleList.setDefault(localeList)
            config.setLocales(localeList)
            val context = context.createConfigurationContext(config)
            config.locale = newLocale
            res.updateConfiguration(config, dm)
            return context
        } else {
            config.setLocale(newLocale)
            val context = context.createConfigurationContext(config)
            config.locale = newLocale
            res.updateConfiguration(config, dm)
            return context
        }

    }

    fun getLocale(context: Context): Locale {
        val config = context.resources.configuration
        return  if(Build.VERSION.SDK_INT >= N) config.locales.get(0) else config.locale
    }
}
