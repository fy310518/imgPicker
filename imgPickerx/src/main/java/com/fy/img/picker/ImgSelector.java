package com.fy.img.picker;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fy.baselibrary.utils.JumpUtils;
import com.fy.bean.ImageFolder;
import com.fy.img.picker.multiselect.ImgPickerActivity;
import com.fy.img.picker.preview.PicturePreviewActivity;

/**
 * description 选择图片，预览大图 入口
 * Created by fangs on 2022/8/5 9:28.
 */
public class ImgSelector {

    public static class Builder {

        private boolean useCamera = true;   //是否使用拍照, 默认 true--> 使用
        private boolean canPreview = true;  // 否可以预览图片，默认为true

        private int maxSelectCount;         // 图片的最大选择数量，小于等于0时，不限数量。
        private int selectionArgsType;      //查询类型：图片，video，img 和 video

        private ImageFolder imgFolder;      // 已经选择的图片 or 预览图片

        private int position;               // 预览图片，首先显示的图片 下标

        private int btnEnable = Color.WHITE;// 预览，完成 按钮 可点击 颜色值


        public Builder setUseCamera(boolean useCamera) {
            this.useCamera = useCamera;
            return this;
        }

        public Builder setCanPreview(boolean canPreview) {
            this.canPreview = canPreview;
            return this;
        }

        public Builder setSingle() {
            this.maxSelectCount = 1;
            return this;
        }

        public Builder setMaxSelectCount(@IntRange(from = 2, to = 9) int maxSelectCount) {
            this.maxSelectCount = maxSelectCount;
            return this;
        }

        public Builder setSelectionArgsType(@IntRange(from = 0, to = 2) int selectionArgsType) {
            this.selectionArgsType = selectionArgsType;
            return this;
        }

        public Builder setImgFolder(@NonNull ImageFolder imgFolder) {
            this.imgFolder = imgFolder;
            return this;
        }

        public Builder setPosition(int position) {
            this.position = position;
            return this;
        }

        public Builder setBtnEnable(int btnEnable) {
            this.btnEnable = btnEnable;
            return this;
        }

        private Bundle getBundle(){
            Bundle bundle = new Bundle();
            bundle.putBoolean(PickerConfig.KEY_ISTAKE_picture, useCamera);
            bundle.putBoolean(PickerConfig.KEY_ISTAKE_canPreview, canPreview);

            bundle.putInt(PickerConfig.KEY_CURRENT_btnEnable, btnEnable);
            bundle.putInt(PickerConfig.KEY_MAX_COUNT, maxSelectCount);
            bundle.putInt(PickerConfig.selectionArgsType, selectionArgsType);
            if (null != imgFolder){
                bundle.putSerializable(PickerConfig.KEY_ALREADY_SELECT, imgFolder);
            }

            return bundle;
        }


        /**
         * 选择图片
         * @param act
         * @param resultCode
         */
        public void start(Activity act, int resultCode){
            JumpUtils.jump(act, ImgPickerActivity.class, getBundle(), resultCode);
        }

        /** 选择图片 */
        public void start(Fragment fragment, int resultCode){
            JumpUtils.jump(fragment, ImgPickerActivity.class, getBundle(), resultCode);
        }


        private Bundle getPreViewBundle(){
            Bundle bundle = new Bundle();
            bundle.putInt(PickerConfig.KEY_MAX_COUNT, -1);
            bundle.putInt(PickerConfig.KEY_CURRENT_POSITION, position);
            bundle.putInt(PickerConfig.KEY_CURRENT_btnEnable, btnEnable);
            bundle.putInt(PickerConfig.KEY_MAX_COUNT, maxSelectCount);

            bundle.putSerializable(PickerConfig.KEY_IMG_FOLDER, imgFolder);

            return bundle;
        }

        /** 预览图片 */
        public void startPreview(Activity act, int resultCode){
            JumpUtils.jump(act, PicturePreviewActivity.class, getPreViewBundle(), resultCode);
        }

        public void startPreview(Fragment fragment, int resultCode){
            JumpUtils.jump(fragment, PicturePreviewActivity.class, getPreViewBundle(), resultCode);
        }
    }

}
