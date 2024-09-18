package com.fy.img.picker;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.fy.img.picker.databinding.ActImgPickerBindingImpl;
import com.fy.img.picker.databinding.ActVideoPalyBindingImpl;
import com.fy.img.picker.databinding.LayoutPreviewBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIMGPICKER = 1;

  private static final int LAYOUT_ACTVIDEOPALY = 2;

  private static final int LAYOUT_LAYOUTPREVIEW = 3;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(3);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.fy.img.picker.R.layout.act_img_picker, LAYOUT_ACTIMGPICKER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.fy.img.picker.R.layout.act_video_paly, LAYOUT_ACTVIDEOPALY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.fy.img.picker.R.layout.layout_preview, LAYOUT_LAYOUTPREVIEW);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIMGPICKER: {
          if ("layout/act_img_picker_0".equals(tag)) {
            return new ActImgPickerBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for act_img_picker is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTVIDEOPALY: {
          if ("layout/act_video_paly_0".equals(tag)) {
            return new ActVideoPalyBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for act_video_paly is invalid. Received: " + tag);
        }
        case  LAYOUT_LAYOUTPREVIEW: {
          if ("layout/layout_preview_0".equals(tag)) {
            return new LayoutPreviewBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for layout_preview is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(2);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    result.add(new com.fy.baselibrary.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(1);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(3);

    static {
      sKeys.put("layout/act_img_picker_0", com.fy.img.picker.R.layout.act_img_picker);
      sKeys.put("layout/act_video_paly_0", com.fy.img.picker.R.layout.act_video_paly);
      sKeys.put("layout/layout_preview_0", com.fy.img.picker.R.layout.layout_preview);
    }
  }
}
