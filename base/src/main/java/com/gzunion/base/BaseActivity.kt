package com.gzunion.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.gzunion.base.ext.startActivityForResultSafely
import kotlinx.android.synthetic.*

abstract class BaseActivity: AppCompatActivity() {

    /**
     * 只使用低 16 bits，但也够用了
     */
    private var mRequestCode = 0

    private val mRequests = mutableMapOf<Int, ForResultCallback>()

    fun forResult(intent: Intent, options: Bundle? = null, cb: ForResultCallback) {
        val code = mRequestCode++
        mRequests[code] = cb
        startActivityForResultSafely(intent, code, options)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val cb = mRequests.remove(requestCode)
        if (cb != null) {
            if (resultCode == Activity.RESULT_OK)
                cb.onSuccess(data)
            else
                cb.onFailure(data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
        mRequests.clear()
        mRequestCode = 0
    }
}

interface ForResultCallback {
    fun onSuccess(data: Intent?) {}
    fun onFailure(data: Intent?) {}
}