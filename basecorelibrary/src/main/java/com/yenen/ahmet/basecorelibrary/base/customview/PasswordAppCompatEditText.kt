package com.yenen.ahmet.basecorelibrary.base.customview

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class PasswordAppCompatEditText constructor(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {

    init{
        this.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        this.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))
        this.maxLines = 1
    }
}