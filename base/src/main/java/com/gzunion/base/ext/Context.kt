package com.gzunion.base.ext

import android.content.Context
import android.content.Intent
import android.widget.Toast

fun Context.toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
    if (msg.isNullOrBlank()) return
    Toast.makeText(this, msg, duration).show()
}

// 打开微信扫一扫
fun Context.wxScan() {
    try {
        val intent = packageManager.getLaunchIntentForPackage("com.tencent.mm")?.apply {
            putExtra("LauncherUI.From.Scaner.Shortcut", true);
            action = "android.intent.action.VIEW";
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    } catch (e: Exception) {
        toast(msg = e.message)
    }
}