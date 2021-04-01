package com.gzunion.uniapp

import android.content.Context
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.annotation.JSONField
import io.dcloud.feature.sdk.DCUniMPJSCallback
import io.dcloud.feature.sdk.DCUniMPSDK
import java.lang.Exception
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import kotlin.jvm.Throws

/**
 * 在这里统一处理小程序发给 app 的消息
 */
class EventHub(
        private val ctx: Context
): DCUniMPSDK.IOnUniMPEventCallBack {

    private val mHandlers = ConcurrentHashMap<String, EventHandler>()
    private val mThreadPool = Executors.newFixedThreadPool(4)

    override fun onUniMPEventReceive(event: String?, data: Any?, cb: DCUniMPJSCallback?) {
        mThreadPool.submit {
            if (event == null)
                return@submit

            val h = mHandlers[event] ?: return@submit
            val arg = runCatching { JSON.parseObject(data as? String, h.dataClz) }.getOrNull()
            try {
                val data = h.process(ctx = ctx, arg = arg)
                cb?.invoke(Response.success(data))
            } catch (e: Exception) {
                cb?.invoke(Response.failure(e.message))
            }
        }
    }

    fun registerHandler(handler: EventHandler) {
        mHandlers[handler.eventName] = handler
    }
}

abstract class EventHandler (
        val eventName: String,
        val dataClz: Class<Any>? = null
) {
    @Throws(Exception::class)
    abstract fun process(ctx: Context, arg: Any?): Any?
}

/**
 * js callback data structure
 */
data class Response(

        @JSONField(name = "code")
        val code: Int,

        @JSONField(name = "error")
        val error: Error? = null,

        @JSONField(name = "data")
        val data: Any? = null
) {
    companion object {

        const val CODE_SUCCESS = 0
        const val CODE_FAILURE = 1

        fun success(data: Any?) = Response(code = CODE_SUCCESS, data = data)

        fun failure(error: String?) = Response(code = CODE_FAILURE, error = Error(message = error))
    }

    data class Error(
            @JSONField(name = "message")
            val message: String? = null
    )
}