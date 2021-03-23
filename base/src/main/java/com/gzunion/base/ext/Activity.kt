package com.gzunion.base.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.zxing.integration.android.IntentIntegrator
import com.gzunion.base.BaseActivity
import com.gzunion.base.ForResultCallback
import com.gzunion.base.R

fun Activity.startActivityForResultSafely(intent: Intent, requestCode: Int, options: Bundle?) {
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(intent, requestCode, options)
    }
}

/**
 * 扫描二维码
 */
fun BaseActivity.scanQrCode(cb: (String) -> Unit) {
    val integrator = IntentIntegrator(this).apply {
        setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        setPrompt(getString(R.string.scan_qr_tip))
        setCameraId(0)
    }
    forResult(intent = integrator.createScanIntent(), cb = object : ForResultCallback {
        override fun onSuccess(data: Intent?) {
            val result = IntentIntegrator.parseActivityResult(Activity.RESULT_OK, data)
            val content = result.contents
            if (content.isNullOrEmpty()) {
                toast(res = R.string.invalid_qr_code)
                return
            }
            cb.invoke(content)
        }

        override fun onFailure(data: Intent?) {
            toast(res = R.string.user_cancel)
        }
    })
}