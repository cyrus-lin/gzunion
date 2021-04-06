package com.gzunion.base.ext

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.gzunion.base.utils.e

fun AppCompatActivity.setStatusBarStyle(
    color: Int = Color.TRANSPARENT,
    lightText: Boolean = true,
    fitsSystemWindows: Boolean = false
) {
    window?.setStatusBarStyle(color, lightText, fitsSystemWindows)
}

/**
 * @param color 状态栏的背景色
 * @param lightText true - 白色文本，false - 黑色文本
 * @param fitsSystemWindows 是否要状态栏占位
 */
@SuppressLint("ObsoleteSdkInt")
fun Window.setStatusBarStyle(
    color: Int = Color.TRANSPARENT,
    lightText: Boolean = true,
    fitsSystemWindows: Boolean = false
) {
    try {
        // 4.4 <= version < 6.0 的沉浸式状态栏；原生 rom 是灰色或灰色渐变，有一点透明度的状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

            // 占位
            val content = findViewById<View>(android.R.id.content) as ViewGroup
            if (content.childCount > 0) {
                content.getChildAt(0).fitsSystemWindows = fitsSystemWindows
            }
        }
        setBackgroundColor(color)
        setStatusTextStyle(dark = !lightText)
    } catch (e: Exception) {
        e(tr = e)
    }
}

private fun Window.setBackgroundColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = color
    }
}

private fun Window.setStatusTextStyle(dark: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        decorView.systemUiVisibility = if (dark)
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        else
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    setStatusTextStyleMiui(dark = dark)
}

/**
 * miui 提供的设置状态栏色系的方案
 */
private fun Window.setStatusTextStyleMiui(dark: Boolean) {
    try {
        val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        val darkModeFlag = field.getInt(layoutParams)

        val clazz = this::class.java
        val extraFlagField = clazz.getMethod(
            "setExtraFlags",
            Int::class.javaPrimitiveType,
            Int::class.javaPrimitiveType
        )
        extraFlagField.invoke(this, if (dark) darkModeFlag else 0, darkModeFlag)
    } catch (ignored: Exception) {}
}