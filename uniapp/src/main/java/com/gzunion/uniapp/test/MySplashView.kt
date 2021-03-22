package com.gzunion.uniapp.test

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import io.dcloud.feature.sdk.Interface.IDCUniMPAppSplashView

class MySplashView : IDCUniMPAppSplashView {
    var splashView: FrameLayout? = null
    override fun getSplashView(context: Context, appid: String): View {
        splashView = FrameLayout(context)
        splashView!!.setBackgroundColor(Color.BLUE)
        val textView = TextView(context)
        textView.text = appid
        textView.setTextColor(Color.WHITE)
        textView.textSize = 20f
        textView.gravity = Gravity.CENTER
        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)
        lp.gravity = Gravity.CENTER
        splashView!!.addView(textView, lp)
        return splashView!!
    }

    override fun onCloseSplash(rootView: ViewGroup) {
        if (rootView != null) rootView.removeView(splashView)
    }
}