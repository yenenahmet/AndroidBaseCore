package com.yenen.ahmet.basecorelibrary.base.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SmsListener(private val serviceProviderNumber: String) : BroadcastReceiver() {

    private var listener: Listener? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent?.action) {
            var smsSender = ""
            val smsBody = StringBuilder()
            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsSender = smsMessage.displayOriginatingAddress
                smsBody.append(smsMessage.messageBody)
            }
            if (smsSender == serviceProviderNumber ) {
                listener?.onTextReceived(smsBody.toString())
            }
        }
    }

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    interface Listener {
        fun onTextReceived(text: String)
    }
}