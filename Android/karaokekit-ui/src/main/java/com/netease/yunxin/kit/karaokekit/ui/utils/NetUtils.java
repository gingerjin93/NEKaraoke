// Copyright (c) 2022 NetEase, Inc. All rights reserved.
// Use of this source code is governed by a MIT license that can be
// found in the LICENSE file.

package com.netease.yunxin.kit.karaokekit.ui.utils;

import android.content.Context;
import com.blankj.utilcode.util.NetworkUtils;
import com.netease.yunxin.kit.alog.ALog;
import com.netease.yunxin.kit.common.ui.utils.ToastUtils;
import com.netease.yunxin.kit.karaokekit.ui.R;

public class NetUtils {
  public static final String TAG = "NetworkUtils";

  public static boolean checkNetwork(Context context) {
    if (context == null) {
      ALog.e(TAG, "checkNetwork but context == null");
      return false;
    }

    if (isConnected()) {
      return true;
    } else {
      ToastUtils.INSTANCE.showShortToast(
          context.getApplicationContext(), context.getString(R.string.karaoke_network_error));
      return false;
    }
  }

  public static void registerStateListener(NetworkUtils.OnNetworkStatusChangedListener listener) {
    NetworkUtils.registerNetworkStatusChangedListener(listener);
  }

  public static void unregisterStateListener(NetworkUtils.OnNetworkStatusChangedListener listener) {
    NetworkUtils.unregisterNetworkStatusChangedListener(listener);
  }

  public static boolean isConnected() {
    return NetworkUtils.isConnected();
  }
}
