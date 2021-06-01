package com.fy.img.picker.preview;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.Validator;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.fy.baselibrary.utils.imgload.imgprogress.ImgLoadCallBack;
import com.fy.baselibrary.utils.notify.L;
import com.fy.baselibrary.widget.RoundProgressBar;
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
    private RoundProgressBar loadProgress;


    private View rvLayout;
    private ImageView imgVideo;
    private ImageView videoPlay;

    public LocalImageHolderView(View itemView, PicturePreviewActivity activity) {
        super(itemView);
        this.activity = activity;
    }

    @Override
    protected void initView(View view) {
        loadProgress = view.findViewById(R.id.loadProgress);

        imageView = view.findViewById(R.id.subImageView);
        imageView.setOnClickListener(v -> activity.toggleStateChange());

        rvLayout = view.findViewById(R.id.rvLayout);
        imgVideo = view.findViewById(R.id.imgVideo);
        imgVideo.setOnClickListener(v -> activity.toggleStateChange());
        videoPlay = view.findViewById(R.id.videoPlay);
        videoPlay.setOnClickListener(v -> {
            ImageItem imgData = (ImageItem) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putString("videoPath", imgData.getPath());
            JumpUtils.jump(activity, VideoPlayActivity.class, bundle);
        });
    }

    @Override
    public void updateUI(ImageItem imgData) {
        if (imgData.getMimeType().equals("video/mp4")){
            rvLayout.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            videoPlay.setTag(imgData);
            ImgLoadUtils.loadImage(imgData.path, R.mipmap.default_image, imgVideo);
            return;
        }

        rvLayout.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);

        if (Validator.isNetAddress(imgData.path)) {
            loadProgress.setVisibility(View.VISIBLE);
            ImgLoadUtils.loadImgProgress(imgData.path, ImgLoadUtils.getDefaultOption(R.mipmap.default_image),
                    imageView, new ImgLoadCallBack<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            loadProgress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            loadProgress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public void onProgress(int progress) {
                            L.e("glide", progress + "%");
                            loadProgress.setProgress(progress);
                        }
                    });
        } else {//加载本地图片
            ImgLoadUtils.loadImage(imgData.path, R.mipmap.default_image, imageView);
        }
    }
}
