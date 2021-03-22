package com.gzunion.app

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.gzunion.base.ApplicationLike
import com.gzunion.uniapp.UniappApplication

class App: Application() {

    private val modules: Array<ApplicationLike> = arrayOf(
        UniappApplication()
    )

    override fun onCreate() {
        super.onCreate()
        modules.forEach { it.onCreate(this) }
    }

    override fun attachBaseContext(base: Context) {
        MultiDex.install(base)
        super.attachBaseContext(base)
        modules.forEach { it.attachBaseContext(base) }
    }
}