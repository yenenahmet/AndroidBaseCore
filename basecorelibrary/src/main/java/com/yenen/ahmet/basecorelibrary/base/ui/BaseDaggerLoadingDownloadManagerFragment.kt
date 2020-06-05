package com.yenen.ahmet.basecorelibrary.base.ui

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.yenen.ahmet.basecorelibrary.base.download.CompleteListener
import com.yenen.ahmet.basecorelibrary.base.download.DownloadManagerListener
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseViewModel

abstract class BaseDaggerLoadingDownloadManagerFragment<VM : BaseViewModel, DB : ViewDataBinding, VDB : ViewDataBinding>(
    viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int, @LayoutRes private val loadingLayoutResId: Int
) :
    BaseDaggerLoadingFragment<VM, DB, VDB>(viewModelClass, layoutRes, loadingLayoutResId) ,CompleteListener{

    private var downloadManagerListener: DownloadManagerListener? = null

    override fun onResult(status: Int, reason: Int, requestId: Long,uri:Uri?,mimeType:String) {
        removeDownloadRequestId(requestId)
        onCompleted(status, reason, requestId,uri,mimeType)
    }

    override fun onResume() {
        super.onResume()
        val downloadManager = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManagerListener = DownloadManagerListener(downloadManager)
        downloadManagerListener?.setListener(this)

        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        activity?.registerReceiver(downloadManagerListener, filter)
    }

    protected fun addDownloadRequestId(requestId: Long){
        downloadManagerListener?.addRequestIds(requestId)
    }

    protected fun removeDownloadRequestId(requestId: Long){
        downloadManagerListener?.removeRequestIds(requestId)
    }

    protected fun clearDownloadRequestIds(){
        downloadManagerListener?.clearRequestIds()
    }

    override fun onPause() {
        super.onPause()
        downloadManagerListener?.let {
            activity?.unregisterReceiver(it)
            it.unBind()
        }
        downloadManagerListener=null
    }

    protected abstract fun onCompleted(status: Int, reason: Int, requestId: Long,uri:Uri?,mimeType: String)

}