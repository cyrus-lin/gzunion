package com.gzunion.uniapp.ext

import android.content.Context
import com.gzunion.base.utils.e
import io.dcloud.feature.sdk.DCUniMPSDK
import java.io.File

data class ProgramInfo(
    val appId: String,
    val versionName: String,
    val versionCode: String,
    val downloadPath: String = ""   // 下载地址
): Comparable<ProgramInfo> {

    /**
     * > 0, this app version is greater than other
     * = 0, this app version is equal with other
     * < 0, less than other
     * especially, [Int.MIN_VALUE] means they are not the same app
     */
    override fun compareTo(other: ProgramInfo): Int {
        if (appId.equals(other.appId, ignoreCase = true)) {
            try {
                return versionCode.toInt() - other.versionCode.toInt()
            } catch (ignored: Exception) {}
        }
        return Int.MIN_VALUE
    }

    operator fun minus(right: ProgramInfo): Int {
        return compareTo(right)
    }
}

/**
 * 获取已安装的小程序列表
 */
fun DCUniMPSDK.installedApps(ctx: Context): List<ProgramInfo> {
    val programs = mutableListOf<ProgramInfo>()
    try {
        File(getAppBasePath(ctx)).listFiles()?.forEach {
            val json = getAppVersionInfo(it.name) ?: return@forEach
            programs.add(ProgramInfo(
                appId = it.name,
                versionName = json.getString("name"),
                versionCode = json.getString("code")
            ))
        }
    } catch (ignored: Exception) {}
    return programs
}

fun DCUniMPSDK.uninstallApps(ctx: Context, appIds: List<String>) {
    if (appIds.isEmpty())
        return
    val dir = File(getAppBasePath(ctx))
    appIds.forEach {
        try {
            File(dir, it).deleteRecursively()
        } catch (e: Exception) {
            e(tr = e)
        }
    }
}

/**
 * 批量安装小程序，文件名作为 ID
 * @param delAfterInstall 安装成功后是否删除文件
 * @return 返回成功安装的数量
 */
fun DCUniMPSDK.installApps(ctx: Context, files: List<File>, delAfterInstall: Boolean = true) {
    if (files.isEmpty())
        return

    val sdk = DCUniMPSDK.getInstance()
    sdk.uninstallApps(ctx = ctx, appIds = files.map { it.nameWithoutExtension })

    files.forEach {
        try {
            sdk.releaseWgtToRunPathFromePath(it.nameWithoutExtension, it.absolutePath) { i: Int, any: Any ->
                if (i == 1) {   // 释放 wgt 成功
                    if (delAfterInstall)
                        it.delete()
                } else {        // 释放 wgt 失败
                    e(tr = Exception("释放 wgt 失败，${it.absolutePath}，$any"))
                }
            }
        } catch (e: Exception) {
            e(tr = e)
        }
    }
}