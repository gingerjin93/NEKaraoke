// Copyright (c) 2022 NetEase, Inc. All rights reserved.
// Use of this source code is governed by a MIT license that can be
// found in the LICENSE file.

package com.netease.yunxin.kit.karaokekit.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.netease.yunxin.kit.karaokekit.ui.statusbar.StatusBarConfig;
import com.netease.yunxin.kit.login.AuthorManager;
import com.netease.yunxin.kit.login.model.EventType;
import com.netease.yunxin.kit.login.model.LoginEvent;
import com.netease.yunxin.kit.login.model.LoginObserver;

public class BaseActivity extends AppCompatActivity {

  private LoginObserver<LoginEvent> loginObserver =
      new LoginObserver<LoginEvent>() {

        @Override
        public void onEvent(LoginEvent event) {
          if (event.getEventType() == EventType.TYPE_LOGOUT && !ignoredLoginEvent()) {
            finish();
            onKickOut();
          }
        }
      };

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //校验APP状态
    validateAppStatus();
    AuthorManager.INSTANCE.registerLoginObserver(loginObserver);
    if (needTransparentStatusBar()) {
      adapterStatusBar();
    } else {
      StatusBarConfig config = provideStatusBarConfig();
      if (config != null) {
        ImmersionBar bar =
            ImmersionBar.with(this)
                .statusBarDarkFont(config.isDarkFont())
                .statusBarColor(config.getBarColor());
        if (config.isFits()) {
          bar.fitsSystemWindows(true);
        }
        if (config.isFullScreen()) {
          bar.fullScreen(true);
        }
        bar.init();
      }
    }
  }

  private void validateAppStatus() {
    int appStatus = AppStatusManager.getInstance().getAppStatus();
    if (appStatus == AppStatusConstant.STATUS_FORCE_KILLED) {
      //异常退出
      finish();
      onKickOut();
    } else if (appStatus == AppStatusConstant.STATUS_NORMAL) {
      //不需要处理或者初始方法调用

    }
  }

  protected boolean needTransparentStatusBar() {
    return false;
  }

  private void adapterStatusBar() {
    // 5.0以上系统状态栏透明
    Window window = getWindow();
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    //会让应用的主体内容占用系统状态栏的空间
    window
        .getDecorView()
        .setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    //将状态栏设置成透明色
    window.setStatusBarColor(Color.TRANSPARENT);
  }

  @Override
  protected void onDestroy() {
    AuthorManager.INSTANCE.unregisterLoginObserver(loginObserver);
    super.onDestroy();
  }

  protected StatusBarConfig provideStatusBarConfig() {
    return null;
  }

  protected boolean ignoredLoginEvent() {
    return false;
  }

  protected void paddingStatusBarHeight(View view) {
    StatusBarConfig.paddingStatusBarHeight(this, view);
  }

  protected void paddingStatusBarHeight(@IdRes int rootViewId) {
    paddingStatusBarHeight(findViewById(rootViewId));
  }

  protected void onKickOut() {}
}
