package com.gzunion.uniapp.eventhandlers

import android.content.Context
import android.content.Intent
import com.alibaba.fastjson.annotation.JSONField
import com.gzunion.base.ext.appKv
import com.gzunion.uniapp.EventException
import com.gzunion.uniapp.EventHandler
import com.gzunion.uniapp.services.DiskInstallService
import io.dcloud.feature.sdk.DCUniMPSDK

/**
 * 小程序发送 JWT 给 app，app 将 JWT 缓存起来
 */
class SyncToken: EventHandler<SyncToken.Companion.Request>(
    "syncToken", dataClz = Request::class.java) {

    override fun process(ctx: Context, sdk: DCUniMPSDK, arg: Request?): Any? {
        val token = arg?.token ?: throw EventException("token can no be null")
        ctx.appKv().edit().putString("token", token).commit()
        ctx.startService(Intent(ctx, DiskInstallService::class.java))
        return token
    }

    companion object {
        data class Request ( @JSONField(name = "token") val token: String? = null )
    }
}