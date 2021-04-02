package com.gzunion.uniapp.services

import android.app.IntentService
import android.content.Intent
import android.os.IBinder
import com.gzunion.uniapp.ext.ProgramInfo
import com.gzunion.uniapp.ext.installedApps
import com.gzunion.uniapp.ext.uninstallApps
import io.dcloud.feature.sdk.DCUniMPSDK

/**
 * 后台静默更新小程序
 * TODO 正在运行的小程序可以卸载并重新安装吗？
 */
class UpgradeService : IntentService("UpgradeService") {

    private val sdk by lazy { DCUniMPSDK.getInstance() }

    override fun onHandleIntent(intent: Intent?) {
        val latest = mutableListOf<ProgramInfo>()   // 获取最新的小程序列表
        val installed = sdk.installedApps(baseContext).toMutableList()

        latest.forEach {
            val p = installed.find { item -> it - item != Int.MIN_VALUE }

            if (p != null) {
                installed.remove(p)
                // 版本一致无需操作
                if (it - p == 0)
                    return@forEach
                // 已安装但版本不同，卸载并升级/降级
                sdk.uninstallApps(baseContext, listOf(p.appId))
            }

            // 没有安装 or 卸载后再安装，从网络下载下来进行安装
            sdk.releaseWgtToRunPathFromePath("appId", "wgtPath") { i: Int, any: Any ->
                if (i == 1) {   // 释放 wgt 成功

                } else {    // 释放 wgt 失败

                }
            }
        }

        // 已安装但没有出现在最新的小程序列表里，需要卸载
        if (installed.isNotEmpty()) {
            sdk.uninstallApps(baseContext, installed.map { it.appId })
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }
}