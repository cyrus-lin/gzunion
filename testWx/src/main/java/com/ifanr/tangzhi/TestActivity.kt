package com.ifanr.tangzhi

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import com.gzunion.base.BaseActivity
import com.gzunion.base.ext.requestPermissionsCompat
import com.gzunion.base.ext.scanQrCode
import com.gzunion.base.ext.toast
import com.gzunion.base.ext.wxScan
import com.gzunion.base.utils.d
import com.gzunion.base.utils.e
import com.gzunion.uniapp.WXAPI
import com.gzunion.uniapp.ext.WxLoginCallback
import com.gzunion.uniapp.ext.installedApps
import com.gzunion.uniapp.ext.login
import com.gzunion.uniapp.services.DiskInstallService
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.dcloud.feature.sdk.DCUniMPSDK
import kotlinx.android.synthetic.main.activity_test_base.*


class TestActivity : BaseActivity() {

    private val sdk by lazy { DCUniMPSDK.getInstance() }

    private val wxapi by lazy {
        val api = WXAPIFactory.createWXAPI(this, null)
        api.registerApp("wx0479d25aff361645")
        return@lazy api
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_base)

        wxScan.setOnClickListener { wxScan() }
        scan.setOnClickListener {
            scanQrCode {
                toast(msg = it)
            }
        }

        diskInstall.setOnClickListener {
            startService(Intent(this, DiskInstallService::class.java))
        }

        refreshInstalledApps.setOnClickListener {
            refreshInstalledApps()
        }

        openApp.setOnClickListener {
            val appId = installedApps.selectedItem as? String ?: return@setOnClickListener
            sdk.startApp(this, appId)
        }

        loginByWx.setOnClickListener {
            WXAPI.login(object : WxLoginCallback {
                override fun onSuccess(token: String) {
                    d(msg = token)
                }

                override fun onFailure(exception: Exception?) {
                    if (exception != null)
                        e(tr = exception)
                    else
                        d(msg = "fail")
                }
            })
        }

        requestPermissionsCompat(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        refreshInstalledApps()
    }

    private fun refreshInstalledApps() {
        val apps = sdk.installedApps(this)
        val adapter = ArrayAdapter(
            this,
            R.layout.text_installed_app_spiner_item,
            apps.map { it.appId })
        installedApps.adapter = adapter
    }
}