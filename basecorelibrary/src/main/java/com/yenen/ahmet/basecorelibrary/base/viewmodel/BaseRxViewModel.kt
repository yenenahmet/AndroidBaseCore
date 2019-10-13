package com.yenen.ahmet.basecorelibrary.base.viewmodel

import io.reactivex.disposables.CompositeDisposable

abstract  class BaseRxViewModel :BaseViewModel() {

    protected val disposable by lazy {
        CompositeDisposable()
    }


    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}