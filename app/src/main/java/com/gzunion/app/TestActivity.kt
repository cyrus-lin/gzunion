package com.gzunion.app

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.widget.ArrayAdapter
import com.gzunion.base.BaseActivity
import com.gzunion.app.R
import com.gzunion.base.WORKERS
import com.gzunion.base.WORKER_HANDLER
import com.gzunion.base.ext.requestPermissionsCompat
import com.gzunion.base.ext.scanQrCode
import com.gzunion.base.ext.toast
import com.gzunion.base.ext.wxScan
import com.gzunion.base.utils.d
import com.gzunion.uniapp.ext.installedApps
import com.gzunion.uniapp.services.DiskInstallService
import io.dcloud.feature.sdk.DCUniMPSDK
import kotlinx.android.synthetic.main.activity_test_base.*


class TestActivity : BaseActivity() {

    private val sdk by lazy { DCUniMPSDK.getInstance() }

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

        requestPermissionsCompat(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        refreshInstalledApps()
    }

    private fun refreshInstalledApps() {
        val apps = sdk.installedApps(this)
        val adapter = ArrayAdapter(this, R.layout.text_installed_app_spiner_item, apps.map { it.appId })
        installedApps.adapter = adapter
    }
}