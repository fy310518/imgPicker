package com.fy.img.picker.preview;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.Validator;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.fy.baselibrary.utils.imgload.imgprogress.ImgLoadCallBack;
import com.fy.baselibrary.utils.notify.L;
import com.fy.bean.ImageItem;
import com.fy.img.picker.R;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * 本地图片Holder
 * Created by fangs on 2017/7/6.
 */
public class LocalImageHolderView extends Holder<ImageItem> {

    private PicturePreviewActivity activity;
    private PhotoView imageView;

    private LinearLayout llProgress;
    private AppCompatImageView imgLoad;
    private TextView tvLoadProgress;

    public LocalImageHolderView(View itemView, PicturePreviewActivity activity) {
        super(itemView);
        this.activity = activity;
    }

    @Override
    protected void initView(View view) {
        llProgress = view.findViewById(R.id.llProgress);
        imgLoad = view.findViewById(R.id.imgLoad);
        tvLoadProgress = view.findViewById(R.id.tvLoadProgress);
        imageView = view.findViewById(R.id.subImageView);
        imageView.setOnClickListener(v -> activity.toggleStateChange());
    }

    @Override
    public void updateUI(ImageItem imgData) {
        if (Validator.isNetAddress(imgData.path)){
            ImgLoadUtils.loadImgProgress(imgData.path, R.mipmap.default_image, imageView, new ImgLoadCallBack<Drawable>(){
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    llProgress.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    llProgress.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public void onProgress(int progress) {
                    L.e("glide", progress + "%");
                    activity.runOnUiThread(() -> tvLoadProgress.setText(ResUtils.getReplaceStr(R.string.loadProgress, progress)));
                }
            });
        } else {//加载本地图片
            ImgLoadUtils.loadImage(imgData.path, R.mipmap.default_image, imageView);
        }
    }
}
