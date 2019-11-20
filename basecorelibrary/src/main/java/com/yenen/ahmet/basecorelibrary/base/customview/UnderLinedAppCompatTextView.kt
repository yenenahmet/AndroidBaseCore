package com.yenen.ahmet.basecorelibrary.base.customview

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class UnderLinedAppCompatTextView
constructor(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    init {
        paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }


    override fun setText(text: CharSequence?, type: BufferType?) {
        paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
        super.setText(text, type)
    }
}