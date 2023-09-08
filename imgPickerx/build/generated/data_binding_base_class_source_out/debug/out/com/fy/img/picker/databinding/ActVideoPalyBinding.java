// Generated by data binding compiler. Do not edit!
package com.fy.img.picker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.fy.img.picker.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActVideoPalyBinding extends ViewDataBinding {
  @NonNull
  public final VideoView videoView;

  protected ActVideoPalyBinding(Object _bindingComponent, View _root, int _localFieldCount,
      VideoView videoView) {
    super(_bindingComponent, _root, _localFieldCount);
    this.videoView = videoView;
  }

  @NonNull
  public static ActVideoPalyBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.act_video_paly, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActVideoPalyBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActVideoPalyBinding>inflateInternal(inflater, R.layout.act_video_paly, root, attachToRoot, component);
  }

  @NonNull
  public static ActVideoPalyBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.act_video_paly, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActVideoPalyBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActVideoPalyBinding>inflateInternal(inflater, R.layout.act_video_paly, null, false, component);
  }

  public static ActVideoPalyBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static ActVideoPalyBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActVideoPalyBinding)bind(component, view, R.layout.act_video_paly);
  }
}