// Generated by data binding compiler. Do not edit!
package com.fy.img.picker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.fy.img.picker.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class LayoutPreviewBinding extends ViewDataBinding {
  @NonNull
  public final ConvenientBanner bannerViewPager;

  @NonNull
  public final CheckBox originalCheckbox;

  @NonNull
  public final FrameLayout pickerBottom;

  @NonNull
  public final TextView send;

  protected LayoutPreviewBinding(Object _bindingComponent, View _root, int _localFieldCount,
      ConvenientBanner bannerViewPager, CheckBox originalCheckbox, FrameLayout pickerBottom,
      TextView send) {
    super(_bindingComponent, _root, _localFieldCount);
    this.bannerViewPager = bannerViewPager;
    this.originalCheckbox = originalCheckbox;
    this.pickerBottom = pickerBottom;
    this.send = send;
  }

  @NonNull
  public static LayoutPreviewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.layout_preview, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static LayoutPreviewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<LayoutPreviewBinding>inflateInternal(inflater, R.layout.layout_preview, root, attachToRoot, component);
  }

  @NonNull
  public static LayoutPreviewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.layout_preview, null, false, component)
   */
  @NonNull
  @Deprecated
  public static LayoutPreviewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<LayoutPreviewBinding>inflateInternal(inflater, R.layout.layout_preview, null, false, component);
  }

  public static LayoutPreviewBinding bind(@NonNull View view) {
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
  public static LayoutPreviewBinding bind(@NonNull View view, @Nullable Object component) {
    return (LayoutPreviewBinding)bind(component, view, R.layout.layout_preview);
  }
}
