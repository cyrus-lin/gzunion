package com.gzunion.uniapp.eventhandlers

import android.content.Context
import com.alibaba.fastjson.annotation.JSONField
import com.gzunion.uniapp.EventHandler
import com.gzunion.uniapp.WXAPI
import com.gzunion.uniapp.ext.WxLoginCallback
import com.gzunion.uniapp.ext.login
import io.dcloud.feature.sdk.DCUniMPSDK
import java.util.concurrent.locks.ReentrantLock

/**
 * 微信登陆
 */
class LoginWithWechat: EventHandler<Unit>(eventName = "loginWithWechat") {

    private val lock by lazy { ReentrantLock() }
    private val cond by lazy { lock.newCondition() }

    override fun process(ctx: Context, sdk: DCUniMPSDK, arg: Unit?): Any? {
        var token: String? = null
        var exception: Exception? = null

        WXAPI.login(object : WxLoginCallback {
            override fun onSuccess(t: String) {
                token = t
                lock.lock()
                try {
                    cond.signalAll()
                } finally {
                    lock.unlock()
                }
            }

            override fun onFailure(e: Exception?) {
                exception = e
                lock.lock()
                try {
                    cond.signalAll()
                } finally {
                    lock.unlock()
                }
            }
        })

        lock.lock()
        try {
            cond.await()
        } finally {
            lock.unlock()
        }

        val t = token
        if (!t.isNullOrEmpty())
            return Response(token = t)

        val e = exception
        if (e != null)
            throw e

        return null
    }

    data class Response (

        @JSONField(name = "token")
        val token: String
        )
}