// Generated by view binder compiler. Do not edit!
package com.fy.img.picker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.fy.img.picker.R;
import java.lang.NullPointerException;
import java.lang.Override;

public final class AdapterCameraBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final FrameLayout camera;

  private AdapterCameraBinding(@NonNull FrameLayout rootView, @NonNull FrameLayout camera) {
    this.rootView = rootView;
    this.camera = camera;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static AdapterCameraBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AdapterCameraBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.adapter_camera, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AdapterCameraBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    FrameLayout camera = (FrameLayout) rootView;

    return new AdapterCameraBinding((FrameLayout) rootView, camera);
  }
}