package com.gzunion.base.utils

import android.util.Log

private const val TAG = "cyrus"

fun e(tag: String = TAG, msg: String? = null, tr: Throwable) {
    Log.e(tag, msg ?: tr.message, tr)
}

fun d(tag: String = TAG, msg: String? = null, tr: Throwable? = null) {
    if (msg.isNullOrEmpty())
        return
    Log.d(tag, msg, tr)
}