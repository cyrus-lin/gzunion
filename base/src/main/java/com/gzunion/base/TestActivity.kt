package com.gzunion.base

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.gzunion.base.ext.scanQrCode
import com.gzunion.base.ext.toast
import com.gzunion.base.ext.wxScan
import kotlinx.android.synthetic.main.activity_test_base.*


class TestActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_base)

        wxScan.setOnClickListener { wxScan() }
        scan.setOnClickListener {
            scanQrCode {
                toast(msg = it)
            }
        }
    }
}