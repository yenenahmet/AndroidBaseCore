package com.yenen.ahmet.basecorelibrary.base.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DownloadManagerListener(private val downloadManager: DownloadManager) : BroadcastReceiver() {

    private val requestIds = mutableListOf<DownloadManagerModel>()
    private var listener: CompleteListener? = null

    fun setListener(listener: CompleteListener) {
        this.listener = null
        this.listener = listener
    }

    fun unBind() {
        this.listener = null
    }

    fun addRequestIds(requestId: Long, notificationVisibility: Int) {
        val model = DownloadManagerModel(requestId, notificationVisibility)
        requestIds.add(model)
    }

    fun removeRequestIds(requestId: Long) {
        var position: Int = -1
        requestIds.forEachIndexed { index, downloadManagerModel ->
            if (downloadManagerModel.requestId == requestId) {
                position = index
            }
        }
        if (position > -1) {
            requestIds.removeAt(position)
        }

    }

    fun clearRequestIds() {
        requestIds.clear()
    }

    fun getDownloadManager(): DownloadManager {
        return downloadManager
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent?.action) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val ids = requestIds.filter { it.requestId == id }
            if (!ids.isNullOrEmpty()) {
                val query = DownloadManager.Query().setFilterById(id)
                var status = 0
                var reason = 0
                downloadManager.query(query)?.use {
                    if (it.moveToFirst()) {
                        val columnIndex = it.getColumnIndex(DownloadManager.COLUMN_STATUS)
                        status = it.getInt(columnIndex)
                        val columnReason = it.getColumnIndex(DownloadManager.COLUMN_REASON)
                        reason = it.getInt(columnReason)
                    }
                }
                val uri = downloadManager.getUriForDownloadedFile(id)
                val mimeType = downloadManager.getMimeTypeForDownloadedFile(id)
                listener?.onResult(status, reason, id, uri, mimeType,ids[0].notificationVisibility)
            }
        }
    }

    data class DownloadManagerModel(
        val requestId: Long,
        val notificationVisibility: Int
    )

}