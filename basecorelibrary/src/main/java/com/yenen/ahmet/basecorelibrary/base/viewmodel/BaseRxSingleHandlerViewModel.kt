package com.yenen.ahmet.basecorelibrary.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// Retrofit İçin //
abstract class BaseRxSingleHandlerViewModel<T> : BaseRxViewModel() {

    // Retrofit -> Service üzerinden dönen Mesaj handler edilir

    // Error. bir error geldiyse burada handle edilir ve errMessage üzerinden tetiklenir
    // -> Bu tetiklenme neticesinde view dinlenerek, view üzerinde güncelleme yapılabilir.
    // Bir uyarı mesajı veya error göre (oluşabilecek view değişimi)
    val errMessage by lazy {
        MutableLiveData<Throwable>()
    }

    // errMessagge ile aynı manttıktır tek farkı service ulaşıldı fakat  içi boş veya data dönmediyse tetiklenir.
    // Yani bir error durumu değil sadece data dönüşü olmaması durumu
    // UI altında yenilenebilir
    val noDataFound by lazy {
        MutableLiveData<String>()
    }


    // service ile iletişim kurulduktan sonra cevap dönerse dönen cevap results değişkeni üzerinden tetiklenir
    private var resultsLiveData: MutableLiveData<T>? = null

    protected var noDataFoundText: String = "Veri Bulunamadı"

    fun getData(): LiveData<T> {
        if (resultsLiveData == null) {
            resultsLiveData = MutableLiveData()
            runFlowable(getServiceFlowable())
        }
        return resultsLiveData as LiveData<T>
    }

    fun postData(): LiveData<T> {
        if (resultsLiveData == null) {
            resultsLiveData = MutableLiveData()
            runFlowable(getServiceFlowable())
        } else {
            dataChangeable()
        }
        return resultsLiveData as LiveData<T>
    }

    fun isNullLiveData(): Boolean {
        return resultsLiveData == null
    }

    // bir kere data çekme işleminden sonra farklı parametrelere göre veya farklı modellere göre istek tekrarlanacaksa
    fun dataChangeable() {
        runFlowable(getServiceFlowable())
    }


    // View Üzerinde getData() çağrıldığında gerekli kontroller sağlanır ve  service methodunun observable'nın çalıştırmak üzere alır
    // her service çağrısının yapısnın yani parametre tipinin kendine has olmasından dolayı
    protected abstract fun getServiceFlowable(): Flowable<T>


    // LiveDataya direk erişim kapatılmıştır // Eğer değere erişilmek istenirse bu fonk üzerinden alınır
    // Nedeni LiveData Etkinliliğinin bozulmaması ve liveDAta için gerekli kullanımın sağlanması. Fazlasına gerek yoktur.
    protected fun getLiveDataValue(): T? {
        return resultsLiveData?.value
    }

    // Bu fonksiyonlar görevini yerine getirip iletimini yapmaktadır extend alınamaz  veya çağrılamaz //
    private fun runFlowable(observable: Flowable<T>) {
        addDisposable(
            observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResults, this::handleError)
        )
    }

    // Eğer Liste tipinde bir model ise listenin boş olup olmadığı kontrol edilir //
    private fun handleResults(results: T?) {
        if (results != null) {
            if (results is List<*>) {
                if (results.isNotEmpty()) {
                    resultsLiveData?.value = results
                } else {
                    noDataFound.value = noDataFoundText
                }
            } else {
                resultsLiveData?.value = results
            }
        } else {
            noDataFound.value = noDataFoundText
        }
    }

    private fun handleError(t: Throwable) {
        errMessage.value = t
    }


}