package com.yenen.ahmet.basecorelibrary.base.customview

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.NumberFormat
import java.util.*

internal fun parseMoneyValue(value: String, groupingSeparator: String, currencySymbol: String): String =
    value.replace(groupingSeparator, "").replace(currencySymbol, "")

internal fun parseMoneyValueWithLocale(
    locale: Locale,
    value: String,
    groupingSeparator: String,
    currencySymbol: String
): Number {
    val valueWithoutSeparator = parseMoneyValue(value, groupingSeparator, currencySymbol)
    return NumberFormat.getInstance(locale).parse(valueWithoutSeparator)!!
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
internal fun getLocaleFromTag(localeTag: String): Locale {
    return try {
        Locale.Builder().setLanguageTag(localeTag).build()
    } catch (e: IllformedLocaleException) {
        Locale.getDefault()
    }
}

internal fun isLollipopAndAbove(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP