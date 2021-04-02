package com.gzunion.uniapp.eventhandlers

import android.content.Context
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.annotation.JSONField
import com.gzunion.uniapp.EventException
import com.gzunion.uniapp.EventHandler
import io.dcloud.feature.sdk.DCUniMPSDK

/**
 * 跳转到内置小程序
 */
class NavigateToUniMP: EventHandler<NavigateToUniMP.Request>(
    eventName = "navigateToUniMP", dataClz = Request::class.java) {

    override fun process(ctx: Context, sdk: DCUniMPSDK, arg: Request?): Any? {
        val appId = arg?.appId ?: throw EventException("appId can not be null")

        var arguments: org.json.JSONObject? = null
        if (arg.extraData != null) {
            arguments = org.json.JSONObject(JSON.toJSONString(arg.extraData))
        }
        sdk.startApp(ctx, appId, null, arg.path, arguments)
        return null
    }

    data class Request (

        @JSONField(name = "appId")
        val appId: String? = null,

        @JSONField(name = "path")
        val path: String? = null,

        @JSONField(name = "extraData")
        val extraData: JSONObject? = null,
    )
}