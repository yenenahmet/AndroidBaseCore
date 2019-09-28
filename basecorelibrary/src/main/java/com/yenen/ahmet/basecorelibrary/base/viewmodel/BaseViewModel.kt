package com.yenen.ahmet.basecorelibrary.base.viewmodel

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    private var viewDataBinding: ViewDataBinding? = null

    fun setViewDataBinding(viewDataBinding: ViewDataBinding) {
        this.viewDataBinding = viewDataBinding
    }


    override fun onCleared() {
        super.onCleared()
        viewDataBinding?.unbind()
        viewDataBinding = null
    }

}