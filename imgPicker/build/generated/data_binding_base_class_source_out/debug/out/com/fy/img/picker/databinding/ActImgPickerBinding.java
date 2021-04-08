// Generated by view binder compiler. Do not edit!
package com.fy.img.picker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.fy.img.picker.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActImgPickerBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final Button btnComplete;

  @NonNull
  public final Button btnDir;

  @NonNull
  public final ImgActivityHeadBinding head;

  @NonNull
  public final RecyclerView recycler;

  private ActImgPickerBinding(@NonNull RelativeLayout rootView, @NonNull Button btnComplete,
      @NonNull Button btnDir, @NonNull ImgActivityHeadBinding head,
      @NonNull RecyclerView recycler) {
    this.rootView = rootView;
    this.btnComplete = btnComplete;
    this.btnDir = btnDir;
    this.head = head;
    this.recycler = recycler;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActImgPickerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActImgPickerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.act_img_picker, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActImgPickerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_complete;
      Button btnComplete = rootView.findViewById(id);
      if (btnComplete == null) {
        break missingId;
      }

      id = R.id.btn_dir;
      Button btnDir = rootView.findViewById(id);
      if (btnDir == null) {
        break missingId;
      }

      id = R.id.head;
      View head = rootView.findViewById(id);
      if (head == null) {
        break missingId;
      }
      ImgActivityHeadBinding binding_head = ImgActivityHeadBinding.bind(head);

      id = R.id.recycler;
      RecyclerView recycler = rootView.findViewById(id);
      if (recycler == null) {
        break missingId;
      }

      return new ActImgPickerBinding((RelativeLayout) rootView, btnComplete, btnDir, binding_head,
          recycler);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}