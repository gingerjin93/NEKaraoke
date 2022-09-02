// Copyright (c) 2022 NetEase, Inc. All rights reserved.
// Use of this source code is governed by a MIT license that can be
// found in the LICENSE file.

package com.netease.yunxin.kit.karaokekit.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.yunxin.kit.karaokekit.ui.R;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

public class FooterView extends LinearLayout implements RefreshFooter {
  private Context context;
  private TextView tvFooter;
  private boolean noMoreData = false;

  public FooterView(Context context) {
    super(context);
    initView(context);
  }

  public FooterView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initView(context);
  }

  public FooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView(context);
  }

  private void initView(Context context) {
    this.context = context;
    LayoutInflater.from(context).inflate(R.layout.refresh_footer_layout, this);
    tvFooter = findViewById(R.id.tv_footer_text);
  }

  @Override
  public boolean setNoMoreData(boolean noMoreData) {
    this.noMoreData = noMoreData;
    if (noMoreData) {
      if (tvFooter != null) {
        tvFooter.setText(context.getString(R.string.karaoke_have_no_more));
      }
    }
    return true;
  }

  @NonNull
  @Override
  public View getView() {
    return this;
  }

  @NonNull
  @Override
  public SpinnerStyle getSpinnerStyle() {
    return SpinnerStyle.Translate;
  }

  @Override
  public void setPrimaryColors(int... colors) {}

  @Override
  public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {}

  @Override
  public void onMoving(
      boolean isDragging, float percent, int offset, int height, int maxDragHeight) {}

  @Override
  public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {}

  @Override
  public void onStartAnimator(
      @NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {}

  @Override
  public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
    return 0;
  }

  @Override
  public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {}

  @Override
  public boolean isSupportHorizontalDrag() {
    return false;
  }

  @Override
  public void onStateChanged(
      @NonNull RefreshLayout refreshLayout,
      @NonNull RefreshState oldState,
      @NonNull RefreshState newState) {
    if (noMoreData) {
      return;
    }

    if (newState == RefreshState.None || newState == RefreshState.PullDownToRefresh) {
      if (tvFooter != null) {
        tvFooter.setText(context.getString(R.string.karaoke_load_more));
      }
    }
  }
}
