package com.gzunion.base

import android.os.Handler
import android.os.HandlerThread
import java.util.concurrent.Executors

val WORKER_HANDLER by lazy {
    val t = HandlerThread("app-worker")
    t.start()
    return@lazy Handler(t.looper)
}

val WORKERS by lazy { Executors.newFixedThreadPool(4) }