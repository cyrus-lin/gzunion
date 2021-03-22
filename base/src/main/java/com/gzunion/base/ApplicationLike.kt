package com.gzunion.base

import android.app.Application
import android.content.Context

interface ApplicationLike {

    fun onCreate(app: Application) {}

    fun attachBaseContext(ctx: Context) {}

}