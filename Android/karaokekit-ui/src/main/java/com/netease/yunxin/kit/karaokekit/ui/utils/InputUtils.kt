/*
 * Copyright (c) 2022 NetEase, Inc. All rights reserved.
 * Use of this source code is governed by a MIT license that can be
 * found in the LICENSE file.
 */

package com.netease.yunxin.kit.karaokekit.ui.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.netease.yunxin.kit.karaokekit.ui.activity.BaseActivity

/**
 * Created by luc on 2020/12/2.
 */
object InputUtils {
    /**
     * 展示软键盘
     *
     * @param inputView 输入框
     */
    fun showSoftInput(inputView: View?) {
        if (inputView == null) {
            return
        }
        val service = inputView.context.applicationContext
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputView.visibility = View.VISIBLE
        inputView.requestFocus()
        service.showSoftInput(inputView, 0)
    }

    /**
     * 隐藏软键盘
     *
     * @param inputView 输入框
     */
    fun hideSoftInput(inputView: View?) {
        if (inputView == null) {
            return
        }
        val service = inputView.context.applicationContext
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        service.hideSoftInputFromWindow(inputView.windowToken, 0)
    }

    /**
     * 添加软键盘弹起高度注册监听
     *
     * @param activity 软键盘所在 页面
     */
    fun registerSoftInputListener(activity: BaseActivity, helper: InputParamHelper) {
        val rootView = activity.window.decorView
        val onGlobalLayoutListener = OnGlobalLayoutListener {
            if (helper.getHeight() <= 0) {
                return@OnGlobalLayoutListener
            }
            val outRect = Rect()
            rootView.getWindowVisibleDisplayFrame(outRect)
            val differ = helper.getHeight() - outRect.bottom
            if (differ == 0) {
                helper.getInputView().visibility = View.GONE
                helper.getInputView().setText("")
                val layoutParams =
                    helper.getInputView().layoutParams as ConstraintLayout.LayoutParams
                layoutParams.bottomMargin = 0
                return@OnGlobalLayoutListener
            }
            helper.getInputView().visibility = View.VISIBLE
            helper.getInputView().requestFocus()
            val layoutParams =
                helper.getInputView().layoutParams as ConstraintLayout.LayoutParams
            layoutParams.bottomMargin = differ
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)

        // 反注册
        activity.lifecycle
            .addObserver(
                LifecycleEventObserver { _: LifecycleOwner?, event: Lifecycle.Event? ->
                    if (event == Lifecycle.Event.ON_DESTROY || event == Lifecycle.Event.ON_PAUSE && activity.isFinishing) {
                        rootView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
                    }
                }
            )
    }

    /**
     * 获取当前屏幕尺寸
     */
    interface InputParamHelper {
        /**
         * 获取屏幕高度
         */
        fun getHeight(): Int

        /**
         * 获取需要输入的view
         */
        fun getInputView(): EditText
    }
}
