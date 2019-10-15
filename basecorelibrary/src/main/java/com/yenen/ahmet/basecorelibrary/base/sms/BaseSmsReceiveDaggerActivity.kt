package com.yenen.ahmet.basecorelibrary.base.sms

import android.content.IntentFilter
import android.os.Bundle
import android.provider.Telephony
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.yenen.ahmet.basecorelibrary.base.ui.BaseDaggerActivity
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseViewModel

abstract class BaseSmsReceiveDaggerActivity<VM : BaseViewModel, DB : ViewDataBinding>(
    viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int,
    private val serviceProviderNumber: String
) : BaseDaggerActivity<VM, DB>(viewModelClass, layoutRes), SmsListener.Listener {

    private var smsListener: SmsListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        smsListener = SmsListener(serviceProviderNumber)
        val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        registerReceiver(smsListener, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        smsListener?.let {
            unregisterReceiver(it)
            it.setListener(null)
        }
        smsListener = null

    }

    override fun onTextReceived(text: String) {
        onTextSmsReceived(text)
    }

    protected abstract fun onTextSmsReceived(text: String)

}