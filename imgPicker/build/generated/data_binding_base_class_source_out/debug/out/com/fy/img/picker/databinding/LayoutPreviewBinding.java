// Generated by view binder compiler. Do not edit!
package com.fy.img.picker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.fy.img.picker.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutPreviewBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConvenientBanner bannerViewPager;

  @NonNull
  public final CheckBox originalCheckbox;

  @NonNull
  public final FrameLayout pickerBottom;

  @NonNull
  public final TextView send;

  private LayoutPreviewBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConvenientBanner bannerViewPager, @NonNull CheckBox originalCheckbox,
      @NonNull FrameLayout pickerBottom, @NonNull TextView send) {
    this.rootView = rootView;
    this.bannerViewPager = bannerViewPager;
    this.originalCheckbox = originalCheckbox;
    this.pickerBottom = pickerBottom;
    this.send = send;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutPreviewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutPreviewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_preview, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutPreviewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bannerViewPager;
      ConvenientBanner bannerViewPager = rootView.findViewById(id);
      if (bannerViewPager == null) {
        break missingId;
      }

      id = R.id.original_checkbox;
      CheckBox originalCheckbox = rootView.findViewById(id);
      if (originalCheckbox == null) {
        break missingId;
      }

      id = R.id.pickerBottom;
      FrameLayout pickerBottom = rootView.findViewById(id);
      if (pickerBottom == null) {
        break missingId;
      }

      id = R.id.send;
      TextView send = rootView.findViewById(id);
      if (send == null) {
        break missingId;
      }

      return new LayoutPreviewBinding((ConstraintLayout) rootView, bannerViewPager,
          originalCheckbox, pickerBottom, send);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
