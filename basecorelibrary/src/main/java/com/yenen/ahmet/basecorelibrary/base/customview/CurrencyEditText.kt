package com.yenen.ahmet.basecorelibrary.base.customview

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import com.yenen.ahmet.basecorelibrary.R
import java.math.BigDecimal
import java.util.*

class CurrencyEditText(context: Context, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {

    private var currencySymbolPrefix = ""
    private var textWatcher: CurrencyInputWatcher
    private var locale: Locale = Locale.getDefault()

    init {
        var useCurrencySymbolAsHint = false
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        var localeTag: String?
        val prefix: String?
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CurrencyEditText,
            0, 0
        ).run {
            try {
                prefix = getString(R.styleable.CurrencyEditText_currencySymbol)
                localeTag = getString(R.styleable.CurrencyEditText_localeTag)
                useCurrencySymbolAsHint = getBoolean(R.styleable.CurrencyEditText_useCurrencySymbolAsHint, false)
            } finally {
                recycle()
            }
        }
        if (!prefix.isNullOrBlank()) currencySymbolPrefix = "$prefix "
        if (useCurrencySymbolAsHint) hint = currencySymbolPrefix
        if (isLollipopAndAbove() && !localeTag.isNullOrBlank()) locale = getLocaleFromTag(localeTag!!)
        textWatcher = CurrencyInputWatcher(this, currencySymbolPrefix, locale)
    }

    fun setLocale(locale: Locale) {
        this.locale = locale
        invalidateTextWatcher()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setLocale(localeTag: String) {
        locale = getLocaleFromTag(localeTag)
        invalidateTextWatcher()
    }

    fun setCurrencySymbol(currencySymbol: String, useCurrencySymbolAsHint: Boolean = false) {
        currencySymbolPrefix = "$currencySymbol "
        if (useCurrencySymbolAsHint) hint = currencySymbolPrefix
        invalidateTextWatcher()
    }

    private fun invalidateTextWatcher() {
        removeTextChangedListener(textWatcher)
        textWatcher = CurrencyInputWatcher(this, currencySymbolPrefix, locale)
        addTextChangedListener(textWatcher)
    }

    fun getNumericValue(): Double {
        return parseMoneyValueWithLocale(
            locale,
            text.toString(),
            textWatcher.decimalFormatSymbols.groupingSeparator.toString(),
            currencySymbolPrefix
        ).toDouble()
    }

    fun getNumericValueBigDecimal(): BigDecimal {
        return BigDecimal(
            parseMoneyValueWithLocale(
                locale,
                text.toString(),
                textWatcher.decimalFormatSymbols.groupingSeparator.toString(),
                currencySymbolPrefix
            ).toString()
        )
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            removeTextChangedListener(textWatcher)
            addTextChangedListener(textWatcher)
            if (text.toString().isEmpty()) setText(currencySymbolPrefix)
        } else {
            removeTextChangedListener(textWatcher)
            if (text.toString() == currencySymbolPrefix) setText("")
        }
    }
}