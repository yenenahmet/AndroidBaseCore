package com.yenen.ahmet.basecorelibrary.base.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*

// ViewToken ve Input Manager Ekran rotasyonları için dikkat edilmesi gerekiyor !  by lazy olarak kullanılmaz//
abstract class BaseViewBindingRecyclerViewInputAdapter<T, VDB : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    private val inputManager: InputMethodManager,
    private val viewToken: RecyclerView
) : BaseViewBindingRecyclerViewAdapter<T, VDB>(layoutRes) {

    protected var textWatcher: TextWatcher? = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            clearTimer()
            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    runOnChange(focusPosition, focusView)
                    hideKeyboard()
                }
            }, changeDelay)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearTimer()
        }

    }

    protected var focusChangeListener: View.OnFocusChangeListener? =
        View.OnFocusChangeListener { v, hasFocus ->
            clearTimer()
            if (hasFocus) {
                focusView = v
                focusPosition = v.tag as Int
            } else {
                focusView = null
                runOnChange(v.tag as Int, v)
            }
        }

    private var changeDelay: Long = 500
    private var timer: Timer? = null
    private var focusPosition: Int = -1
    private var focusView: View? = null
    private var isScroll: Boolean = false
    private var onScrollListener: RecyclerView.OnScrollListener? = null

    init {
        scrollListener()
    }

    // RecyclerView Scroll listener //
    private fun scrollListener() {
        onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                clearTimer()
                if (inputManager.isActive) {
                    clearFocusView()
                    hideKeyboard()
                }
                isScroll = newState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE
            }
        }
        viewToken.addOnScrollListener(onScrollListener!!)
    }
    // RecyclerView Scroll listener //

    // Class Fun //
    private fun runOnChange(position: Int, view: View?) {
        clearTimer()
        val item = getItem(position)
        if (position > -1 && item != null && view != null && !isScroll) {
            onChange(position, view, item)
        }
    }

    private fun hideKeyboard() {
        inputManager.hideSoftInputFromWindow(viewToken.windowToken, 0)
    }

    override fun unBind() {
        super.unBind()
        focusChangeListener = null
        textWatcher = null
        clearTimer()
        timer = null
        focusView = null
        onScrollListener?.let {
            viewToken.removeOnScrollListener(it)
        }
        onScrollListener = null
    }

    private fun clearTimer() {
        timer?.cancel()
        timer?.purge()
    }

    fun clearFocusView() {
        focusView?.clearFocus()
    }
    // Class Fun //

    //  Abstract fun //
    protected abstract fun onChange(position: Int, view: View, item: T)

    override fun onUseBindViewHolder(item: T, position: Int, binding: VDB) {
        onInputConfig(item, position, binding)
    }

    protected abstract fun onInputConfig(item: T, position: Int, binding: VDB)
    //  Abstract fun //

}