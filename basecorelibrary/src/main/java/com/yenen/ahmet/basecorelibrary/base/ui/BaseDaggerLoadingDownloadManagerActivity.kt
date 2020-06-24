package com.yenen.ahmet.basecorelibrary.base.ui

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.core.content.FileProvider
import androidx.databinding.ViewDataBinding
import com.yenen.ahmet.basecorelibrary.base.download.CompleteListener
import com.yenen.ahmet.basecorelibrary.base.download.DownloadManagerListener
import com.yenen.ahmet.basecorelibrary.base.utility.FileUtils
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseViewModel
import java.io.File
import java.lang.Exception

abstract class BaseDaggerLoadingDownloadManagerActivity<VM : BaseViewModel, DB : ViewDataBinding, VDB : ViewDataBinding>(
    viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int, @LayoutRes private val loadingLayoutResId: Int
) : BaseDaggerLoadingActivity<VM, DB, VDB>(viewModelClass, layoutRes, loadingLayoutResId),
    CompleteListener {

    private var downloadManagerListener: DownloadManagerListener? = null

    override fun onResult(status: Int, reason: Int, requestId: Long, uri: Uri?, mimeType: String) {
        removeDownloadRequestId(requestId)
        onCompleted(status, reason, requestId, uri, mimeType, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManagerListener = DownloadManagerListener(downloadManager)
        downloadManagerListener?.setListener(this)

        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(downloadManagerListener, filter)
    }

    private fun addDownloadRequestId(requestId: Long) {
        downloadManagerListener?.addRequestIds(requestId)
    }

    protected fun removeDownloadRequestId(requestId: Long) {
        downloadManagerListener?.removeRequestIds(requestId)
    }

    protected fun clearDownloadRequestIds() {
        downloadManagerListener?.clearRequestIds()
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadManagerListener?.let {
            unregisterReceiver(it)
            it.unBind()
        }
        downloadManagerListener = null
    }

    protected fun downloadManagerDownload(
        notificationVisibility: Int,
        path: String,
        fileName: String,
        token: String?,
        downloadAgain: Boolean,
        authority:String
    ) {
        try {
            if(downloadAgain){
                download(notificationVisibility, path, fileName, token)
            }else{
                val file = getFile(fileName)
                if (!file.exists()) {
                    download(notificationVisibility, path, fileName, token)
                } else {
                    val uri  =if( Build.VERSION.SDK_INT >=  Build.VERSION_CODES.LOLLIPOP_MR1){
                       FileProvider.getUriForFile(
                            this,
                            authority,
                            file
                        )
                    }else{
                        Uri.withAppendedPath(
                            Uri.fromFile(getExternalFilesDir(null)),
                            fileName
                        )
                    }

                    val mimeType = FileUtils.getMimeType(fileName)
                    onCompleted(0, 0, 0, uri, mimeType, "")
                }
            }
        } catch (ex: Exception) {
            onCompleted(-1, -1, -1, null, "", ex.toString())
        }
    }


    private fun download(
        notificationVisibility: Int,
        path: String,
        fileName: String,
        token: String? ) {
        val request = DownloadManager.Request(Uri.parse(path)).apply {
            setTitle(fileName)
            setDescription("Dosya indiriliyor...")
            setNotificationVisibility(notificationVisibility)
            setDestinationInExternalFilesDir(this@BaseDaggerLoadingDownloadManagerActivity, null, fileName)
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            token?.let {
                addRequestHeader("Authorization", it)
            }
        }
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val req = downloadManager.enqueue(request)
        addDownloadRequestId(req)
    }

    private fun getFile(fileName: String): File {
        return File(getExternalFilesDir(null), fileName)
    }

    protected abstract fun onCompleted(
        status: Int,
        reason: Int,
        requestId: Long,
        uri: Uri?,
        mimeType: String,
        err: String
    )
}