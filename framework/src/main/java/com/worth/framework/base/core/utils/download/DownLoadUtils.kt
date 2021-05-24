package com.worth.framework.base.core.utils.download

import android.text.TextUtils
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadLargeFileListener
import com.liulishuo.filedownloader.FileDownloader
import com.worth.framework.base.core.utils.L
import com.worth.framework.base.core.utils.application

class DownLoadUtils {

    //下载状态监听
    private var onDownLoadListener: OnDownLoadListener? = null
    private val fileDownloader: FileDownloader = FileDownloader()

    private var fileDownloadLargeFileListener : FileDownloadLargeFileListener? = null

    init {
        FileDownloader.setup(application)
    }

    /**
     * 下载文件
     *
     * @param url  URL地址
     * @param path 下载后存储路径
     */
    fun downLoadFile(url: String?, path: String?, isShowTips : Boolean = true) {
        if (TextUtils.isEmpty(url)) {
            onDownLoadListener?.error(Exception("download url is null"))
            return
        }
        L.e("下载地址 == $url")
        fileDownloadLargeFileListener = object : FileDownloadLargeFileListener() {
            override fun pending(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {}
            override fun progress(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {
                onDownLoadListener?.run {
                    val progress = (soFarBytes.toDouble() / totalBytes.toDouble() * 100).toInt()
                    progress(progress)
                }
            }

            override fun blockComplete(task: BaseDownloadTask) {}
            override fun completed(task: BaseDownloadTask) {
                onDownLoadListener?.completed()
            }

            override fun retry(
                task: BaseDownloadTask,
                ex: Throwable,
                retryingTimes: Int,
                soFarBytes: Long
            ) {
                super.retry(task, ex, retryingTimes, soFarBytes)
                if (isShowTips) {
                    //下载失败
                }
            }

            override fun paused(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {
                onDownLoadListener?.onPause()
            }

            override fun error(task: BaseDownloadTask, e: Throwable) {
                onDownLoadListener?.error(e)
            }

            override fun warn(task: BaseDownloadTask) {}
        }

        FileDownloader.getImpl()
            .create(url)
            .setPath(path)
            .setAutoRetryTimes(5) //重试次数
            .setListener(fileDownloadLargeFileListener).start()
    }

    /**
     * 暂停下载
     */
    fun pause() {
        fileDownloadLargeFileListener?.run {
            fileDownloader.pause(fileDownloadLargeFileListener)
        }
    }

    fun pauseAll() {
        fileDownloader.pauseAll()
    }

    fun setOnDownLoadListener(onDownLoadListener: OnDownLoadListener?) {
        this.onDownLoadListener = onDownLoadListener
    }

}