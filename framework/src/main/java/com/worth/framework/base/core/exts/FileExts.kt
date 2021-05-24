package com.worth.framework.base.core.exts

import com.worth.framework.base.core.base.constants.separator
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

/**
 * 删除文件夹
 */
fun deleteFiles(root: String?) {
    root?.run {
        val rootFile = File(this)
        if (!rootFile.exists()) {
            return
        } else {
            rootFile.parent?.run {
                val path = this + separator + System.currentTimeMillis()
                if (rootFile.isFile) {
                    deleteFile(path, rootFile)
                } else if (rootFile.isDirectory) {
                    val childFile = rootFile.listFiles()
                    if (childFile == null || childFile.isEmpty()) {
                        deleteFile(path, rootFile)
                    } else {
                        for (f in childFile) {
                            deleteFiles(f.path)
                        }
                        deleteFile(path, rootFile)
                    }
                }
            }
        }
    }
}

fun deleteFile(path: String, rootFile: File) {
    //解决open failed: EBUSY (Device or resource busy)
    val to = File(path)
    rootFile.renameTo(to)
    to.delete()
}

fun fileGetMD5(file: File?): String? {
    var fis: FileInputStream? = null
    val s: String?
    val sTmp: String?
    return try {
        val md = MessageDigest.getInstance("MD5")
        fis = FileInputStream(file)
        val buffer = ByteArray(2048)
        var length = -1
        while (fis.read(buffer).also { length = it } != -1) {
            md.update(buffer, 0, length)
        }
        val tmp = md.digest()
        sTmp = getHexString(tmp)
        s = exChange(sTmp)
        s
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    } finally {
        try {
            fis?.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}

fun getHexString(bytes: ByteArray): String? {
    var result = ""
    for (i in bytes.indices) {
        result += Integer.toHexString(bytes[i].toInt() and 0xFF).let {
            if (it.length < 2) {
                "0${it}"
            } else {
                it
            }
        }
    }
    return result
}

fun exChange(str: String?): String? {
    val sb = StringBuffer()
    if (str != null) {
        for (element in str) {
            when {
                Character.isUpperCase(element) -> {
                    sb.append(Character.toLowerCase(element))
                }
                Character.isLowerCase(element) -> {
                    sb.append(Character.toUpperCase(element))
                }
                else -> {
                    sb.append(element)
                }
            }
        }
    }
    return sb.toString()
}