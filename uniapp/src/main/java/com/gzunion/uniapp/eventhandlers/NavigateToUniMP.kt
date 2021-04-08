package com.gzunion.uniapp.eventhandlers

import android.content.Context
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.annotation.JSONField
import com.gzunion.base.utils.e
import com.gzunion.uniapp.EventException
import com.gzunion.uniapp.EventHandler
import io.dcloud.feature.sdk.DCUniMPSDK
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

/**
 * 跳转到内置小程序
 */
class NavigateToUniMP: EventHandler<NavigateToUniMP.Request>(
    eventName = "navigateToUniMP", dataClz = Request::class.java) {

    private val lock by lazy { ReentrantLock() }

    private val cond by lazy { lock.newCondition() }

    override fun process(ctx: Context, sdk: DCUniMPSDK, arg: Request?): Any? {
        val appId = arg?.appId ?: throw EventException("appId can not be null")

        // 必须先关闭当前小程序，然后稍等一会再打开新的小程序
        // 否则会出现：后续打开小程序的请求都指向同一小程序、新打开小程序点击事件异常...
        if (!sdk.closeCurrentApp())
            e(tr = Exception("NavigateToUniMP closeCurrentApp fail"))
        await()

        var arguments: org.json.JSONObject? = null
        if (arg.extraData != null) {
            arguments = org.json.JSONObject(JSON.toJSONString(arg.extraData))
        }
        sdk.startApp(ctx, appId, null, arg.path, arguments)
        return null
    }

    private fun await(millis: Long = 1000) {
        lock.lock()
        try {
            cond.await(millis, TimeUnit.MILLISECONDS)
        } finally {
            lock.unlock()
        }
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