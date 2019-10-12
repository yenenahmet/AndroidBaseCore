package com.yenen.ahmet.basecorelibrary.base.local

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Build.VERSION_CODES.N
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
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)

        config.setLocale(locale)
        var mContext: Context = context
        mContext = context.createConfigurationContext(config)
        return mContext
    }

    fun getLocale(context: Context): Locale {
        val config = context.resources.configuration
        return  if(Build.VERSION.SDK_INT >= N) config.locales.get(0) else config.locale
    }
}
