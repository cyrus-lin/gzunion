package com.gzunion.base.utils

import android.util.Log

private const val TAG = "gzunion"

fun e(tag: String = TAG, msg: String? = null, throwable: Throwable) {
    Log.e(tag, msg ?: throwable.message, throwable)
}