package com.gzunion.uniapp.test

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.Nullable
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.gzunion.app.R
import io.dcloud.feature.sdk.DCUniMPSDK.*
import org.json.JSONObject

class TestActivity : AppCompatActivity() {
    var mContext: Context? = null
    var mHandler: Handler? = null
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mHandler = Handler()
        setContentView(R.layout.activity_test)
        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            try {
                getInstance().startApp(mContext, "__UNI__04E3A11", MySplashView::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            try {
                getInstance().startApp(mContext, "__UNI__04E3A11")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val button3 = findViewById<Button>(R.id.button3)
        button3.setOnClickListener {
            try {
                //"pages/tabBar/extUI/extUI" "pages/component/scroll-view/scroll-view"
                getInstance()
                    .startApp(
                        mContext,
                        "__UNI__04E3A11",
                        "pages/tabBar/extUI/extUI?aaa=123&bbb=456"
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val button4 = findViewById<Button>(R.id.button4)
        button4.setOnClickListener {
            try {
                getInstance()
                    .startApp(mContext, "__UNI__04E3A11", "pages/component/view/view")
                mHandler!!.postDelayed({
                    Log.e("unimp", "延迟5秒结束 开始关闭当前小程序")
                    getInstance().closeCurrentApp()
                }, 5000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val button5 = findViewById<Button>(R.id.button5)
        button5.setOnClickListener {
            val info = getInstance().getAppVersionInfo("__UNI__04E3A11")
            if (info != null) {
                Log.e("unimp", "info===$info")
            }
        }
        val button6 = findViewById<Button>(R.id.button6)
        button6.setOnClickListener {
            try {
                getInstance().startApp(mContext, "__UNI__2108B0A")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val button7 = findViewById<Button>(R.id.button7)
        button7.setOnClickListener {
            try {
                getInstance().startApp(mContext, "__UNI__2108B0A", "pages/sample/send-event")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val button8 = findViewById<Button>(R.id.button8)
        button8.setOnClickListener {
            try {
                getInstance().startApp(mContext, "__UNI__2108B0A", "pages/sample/ext-module")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        getInstance().setDefMenuButtonClickCallBack { appid, id ->
            when (id) {
                "gy" -> {
                    Log.e("unimp", "点击了关于$appid")
                    //宿主主动触发事件
                    val data = JSONObject()
                    try {
                        data.put("sj", "点击了关于")
                        getInstance().sendUniMPEvent("gy", data)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                "hqdqym" -> {
                    Log.e(
                        "unimp",
                        "当前页面url=" + getInstance().currentPageUrl
                    )
                }
                "gotoTestPage" -> {
                }
            }
        }
        getInstance().setUniMPOnCloseCallBack { appid ->
            Log.e("unimp", appid + "被关闭了")
            Toast.makeText(mContext, appid + "被关闭了", Toast.LENGTH_SHORT).show()
        }
        getInstance().setOnUniMPEventCallBack { event, data, callback ->
            Log.i("cs", "onUniMPEventReceive    event=$event")
            //回传数据给小程序
            callback.invoke("收到消息")
        }
        checkPermission()
    }

    /**
     * 检查并申请权限
     */
    fun checkPermission() {
        var targetSdkVersion = 0
        val PermissionString = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        try {
            val info = this.packageManager.getPackageInfo(this.packageName, 0)
            targetSdkVersion = info.applicationInfo.targetSdkVersion //获取应用的Target版本
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Build.VERSION.SDK_INT是获取当前手机版本 Build.VERSION_CODES.M为6.0系统
            //如果系统>=6.0
            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                //第 1 步: 检查是否有相应的权限
                val isAllGranted = checkPermissionAllGranted(PermissionString)
                if (isAllGranted) {
                    Log.e("err", "所有权限已经授权！")
                    return
                }
                // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
                ActivityCompat.requestPermissions(this, PermissionString, 1)
            }
        }
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private fun checkPermissionAllGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                // 只要有一个权限没有被授予, 则直接返回 false
                //Log.e("err","权限"+permission+"没有授权");
                return false
            }
        }
        return true
    }
}