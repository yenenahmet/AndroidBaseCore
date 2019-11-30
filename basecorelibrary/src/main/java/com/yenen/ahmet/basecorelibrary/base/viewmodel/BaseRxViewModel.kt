package com.yenen.ahmet.basecorelibrary.base.viewmodel

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseRxViewModel : BaseViewModel() {

    private val disposable by lazy {
        CompositeDisposable()
    }

    fun addDisposable(d: Disposable) {
        disposable.add(d)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}