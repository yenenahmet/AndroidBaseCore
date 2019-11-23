package com.yenen.ahmet.basecorelibrary.base.remote

import io.reactivex.Emitter
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.Callable

object PageRxInputStream {

    fun getFlowableData(url: String, charsetName: String): Flowable<String> {
        return Flowable.generate(
            Callable {
                val urlConnection = URL(url)
                val bufferedInputStream = BufferedInputStream(urlConnection.openStream())
                BufferedReader(InputStreamReader(bufferedInputStream, charsetName))
            },
            BiConsumer<BufferedReader, Emitter<String>> { bufferedReader, stringEmitter ->
                val line = bufferedReader.readLine()
                if (line != null) {
                    stringEmitter.onNext(line)
                } else {
                    stringEmitter.onComplete()
                }
            }, Consumer { bufferedReader ->
                try {
                    bufferedReader.close()
                } catch (ignore: Exception) {

                }
            }
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}