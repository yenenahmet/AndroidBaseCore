package com.yenen.ahmet.basecorelibrary.base.binding

import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter

object ViewBindings {

    @BindingAdapter("setHtmlText")
    @JvmStatic
    fun bindHtmlText(view: AppCompatTextView, value: String?) {
        if (value.isNullOrEmpty()) {
            return
        }
        view.text = HtmlCompat.fromHtml(value, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}