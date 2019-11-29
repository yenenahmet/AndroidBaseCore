package com.yenen.ahmet.basecorelibrary.base.adapter

import android.app.Activity
import android.content.Context
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
    @LayoutRes private val layoutRes: Int
) : BaseViewBindingRecyclerViewAdapter<T, VDB>(layoutRes) {

    private var activity: Activity? = null
    private var inputManager: InputMethodManager? = null
    private var recyclerView: RecyclerView? = null

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

    private var changeDelay: Long = 1000
    private var timer: Timer? = null
    private var focusPosition: Int = -1
    private var focusView: View? = null
    private var isScroll: Boolean = false
    private var onScrollListener: RecyclerView.OnScrollListener? = null

    // Adapter Oluşturulduktan sonra önce setActivity sonra setScrollListener Kurulur//
    // Adapter her seferinde oluturuşmasına gerek yok fakat rotasyonlarda bu iki fonksiyon yenilenmesi gerekiyor //
    fun setScrollListener(recyclerView: RecyclerView) {
        removeScrollListener()
        onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                clearTimer()
                inputManager?.let {
                    if (it.isActive) {
                        clearFocusView()
                        hideKeyboard()
                    }
                }

                isScroll = newState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE
            }
        }
        recyclerView.addOnScrollListener(onScrollListener!!)
        this.recyclerView = recyclerView
    }

    fun setActivity(activity: Activity) {
        this.activity = null
        this.inputManager = null
        this.activity = activity
        this.inputManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    // Class Fun //
    private fun runOnChange(position: Int, view: View?) {
        clearTimer()
        getItem(position)?.let {
            if (position > -1 && view != null && !isScroll) {
                activity?.runOnUiThread {
                    onChange(position, view, it)
                }
            }
        }
    }

    private fun hideKeyboard() {
        recyclerView?.let {
            inputManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun unBind() {
        super.unBind()
        focusChangeListener = null
        textWatcher = null
        clearTimer()
        timer = null
        focusView = null
        removeScrollListener()
        recyclerView = null
        activity = null
        inputManager = null
    }

    private fun removeScrollListener() {
        onScrollListener?.let {
            recyclerView?.removeOnScrollListener(it)
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

    //  Abstract fun //

    fun setChangeDelay(changeDelay: Long) {
        this.changeDelay = changeDelay
    }

}