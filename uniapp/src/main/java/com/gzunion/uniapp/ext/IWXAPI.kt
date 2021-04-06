package com.gzunion.uniapp.ext

import android.support.annotation.MainThread
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI

interface WxLoginCallback {
    fun onSuccess(token: String) {}
    fun onFailure(exception: Exception?) {}
}

var LOGIN_CB: WxLoginCallback? = null

/**
 * 拉起微信登陆页面
 */
@MainThread
fun IWXAPI.login(cb: WxLoginCallback): Boolean {
    val req = SendAuth.Req().apply {
        scope = "snsapi_userinfo"
        state = "wechat_sdk_demo_test"
    }
    LOGIN_CB = cb

    val result = sendReq(req)
    if (!result)
        LOGIN_CB = null
    return result
}