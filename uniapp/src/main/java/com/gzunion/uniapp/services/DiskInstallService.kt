package com.gzunion.uniapp.services

import android.app.IntentService
import android.content.Intent
import android.os.Environment
import com.gzunion.base.utils.d
import com.gzunion.base.utils.e
import com.gzunion.uniapp.ext.installApps
import io.dcloud.feature.sdk.DCUniMPSDK
import java.util.concurrent.atomic.AtomicInteger

/**
 * 覆盖安装 sdcard 根目录下的小程序包
 */
class DiskInstallService : IntentService("DiskInstallService") {

    private val sdk by lazy { DCUniMPSDK.getInstance() }
    private val ctx by lazy { applicationContext }
    private val status = AtomicInteger(0)   // 1 - 安装工作中

    override fun onHandleIntent(intent: Intent?) {
        if (status.compareAndSet(0, 1)) {
            try {
                val sdcard = Environment.getExternalStorageDirectory() ?: return
                val files = sdcard.listFiles()?.filter { it.isFile && it.canRead() &&
                        it.absolutePath.endsWith(suffix = "wgt", ignoreCase = false) }
                d(msg = "${files?.size} wgt in sdcard to be install")
                if (files.isNullOrEmpty()) return
                sdk.installApps(ctx = ctx, files = files)
            } catch (e: Exception) {
                e(tr = e)
            }
            status.set(0)
        }
    }
}