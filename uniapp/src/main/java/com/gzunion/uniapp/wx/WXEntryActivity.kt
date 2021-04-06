package com.gzunion.uniapp.wx

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.gzunion.base.BaseActivity
import com.gzunion.base.ext.setStatusBarStyle
import com.gzunion.uniapp.WXAPI
import com.gzunion.uniapp.ext.LOGIN_CB
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

class WXEntryActivity: BaseActivity(), IWXAPIEventHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.TRANSPARENT)
        })
        setStatusBarStyle()
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        try {
            if (!WXAPI.handleIntent(intent, this)) {
                onResult()
            }
        } catch (e: Exception) {
            onResult(exception = e)
        }
    }

    override fun onReq(req: BaseReq?) {
        onResult()
    }

    override fun onResp(resp: BaseResp?) {
        when (resp) {
            is SendAuth.Resp -> when (resp.errCode) {
                BaseResp.ErrCode.ERR_OK -> onResult(token = resp.code)
                else -> onResult(exception = WxException(resp))
            }
            else -> onResult()
        }
    }

    private fun onResult(token: String? = null, exception: Exception? = null) {
        val cb = LOGIN_CB ?: return
        if (!token.isNullOrEmpty())
            cb.onSuccess(token)
        else
            cb.onFailure(exception)
        LOGIN_CB = null
        finish()
    }
}