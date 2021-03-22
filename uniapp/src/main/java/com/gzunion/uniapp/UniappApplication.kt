package com.gzunion.uniapp

import android.app.Application
import android.util.Log
import com.gzunion.base.ApplicationLike
import com.taobao.weex.WXSDKEngine
import com.taobao.weex.common.WXException
import io.dcloud.feature.sdk.DCSDKInitConfig
import io.dcloud.feature.sdk.DCUniMPSDK
import io.dcloud.feature.sdk.DCUniMPSDK.IDCUNIMPPreInitCallback
import io.dcloud.feature.sdk.MenuActionSheetItem
import java.util.*

class UniappApplication : ApplicationLike {

    override fun onCreate(app: Application) {
        // 初始化 uni 小程序 SDK ---- start ----------
        val item = MenuActionSheetItem("关于", "gy")
        val item1 = MenuActionSheetItem("获取当前页面url", "hqdqym")
        val item2 = MenuActionSheetItem("跳转到宿主原生测试页面", "gotoTestPage")
        val sheetItems: MutableList<MenuActionSheetItem> = ArrayList()
        sheetItems.add(item)
        sheetItems.add(item1)
        sheetItems.add(item2)
        val config = DCSDKInitConfig.Builder()
            .setCapsule(true)
            .setMenuDefFontSize("16px")
            .setMenuDefFontColor("#ff00ff")
            .setMenuDefFontWeight("normal")
            .setMenuActionSheetItems(sheetItems)
            .setEnableBackground(true) // 开启后台运行
            .build()
        DCUniMPSDK.getInstance().initialize(app, config) { b ->
            Log.i("unimp", "onInitFinished----$b")
        }
        // 初始化 uni 小程序 SDK ---- end ----------
    }
}