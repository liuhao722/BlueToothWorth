package com.worth.framework.base.core.utils.download

/**
 * 下载进度监听
 */
interface OnDownLoadListener {
    /**
     * 下载进度百分比
     *
     * @param progress
     */
    fun progress(progress: Int)

    /**
     * 下载完成
     */
    fun completed()

    /**
     * 下载错误
     *
     * @param e
     */
    fun error(e: Throwable?)

    /**
     *
     */
    fun onPause()
}