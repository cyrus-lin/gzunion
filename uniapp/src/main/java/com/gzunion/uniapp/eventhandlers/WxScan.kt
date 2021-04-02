package com.gzunion.uniapp.eventhandlers

import android.content.Context
import com.gzunion.base.ext.wxScan
import com.gzunion.uniapp.EventHandler
import io.dcloud.feature.sdk.DCUniMPSDK

/**
 * 打开微信扫一扫
 */
class WxScan: EventHandler<Unit>(eventName = "invokeWxScanQrcode") {
    override fun process(ctx: Context, sdk: DCUniMPSDK, arg: Unit?): Any? {
        ctx.wxScan()
        return null
    }
}