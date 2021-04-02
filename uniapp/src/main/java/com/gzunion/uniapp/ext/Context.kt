package com.gzunion.uniapp.ext

import android.content.Context
import com.gzunion.base.ext.toast
import com.gzunion.base.utils.e
import io.dcloud.feature.sdk.DCUniMPSDK
import io.dcloud.feature.sdk.Interface.IDCUniMPAppSplashView
import org.json.JSONObject

fun Context.startUniApp(
        appId: String,
        splashClass: Class<IDCUniMPAppSplashView>? = null,
        redirectPath: String? = null,
        arguments: JSONObject? = null) {
    try {
        DCUniMPSDK.getInstance().startApp(this, appId, splashClass, redirectPath, arguments)
    } catch (e: Exception) {
        e(tr = e)
        toast(msg = e.message)
    }
}