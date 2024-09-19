package com.fy.img.picker.preview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.rv.adapter.OnListener;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.Validator;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.fy.baselibrary.utils.imgload.imgprogress.ImgLoadCallBack;
import com.fy.baselibrary.utils.notify.L;
import com.fy.baselibrary.widget.RoundProgressBar;
import com.fy.bean.ImageItem;
import com.fy.img.picker.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

/**
 * description TODO
 * Created by fangs on 2024/9/19 9:44.
 */
public class LocalImageListAdapter extends RvCommonAdapter<ImageItem, ViewHolder> {
    PicturePreviewActivity activity;

    public LocalImageListAdapter(PicturePreviewActivity activity, List<ImageItem> datas) {
        super(activity, R.layout.viewpager_preview, datas);
        this.activity = activity;

        setItemClickListener(new OnListener.OnitemClickListener() {
            @Override
            public void onItemClick(View view) {
                ImageItem imgData = (ImageItem) view.getTag();

                if (view.getId() == R.id.subImageView){
                    activity.toggleStateChange();
                } else if (view.getId() == R.id.imgVideo){
                    activity.toggleStateChange();
                } else if (view.getId() == R.id.videoPlay){
                    Bundle bundle = new Bundle();
                    bundle.putString("videoPath", imgData.getPath());
                    JumpUtils.jump(activity, VideoPlayActivity.class, bundle);
                }
            }
        });
    }

    @Override
    protected void bindOnClick(ViewHolder viewHolder) {
        super.bindOnClick(viewHolder);
        viewHolder.setOnClickListener(R.id.subImageView, this);
        viewHolder.setOnClickListener(R.id.imgVideo, this);
        viewHolder.setOnClickListener(R.id.videoPlay, this);
    }

    @Override
    protected void setViewTag(ViewHolder holder, ImageItem imageItem) {
        super.setViewTag(holder, imageItem);
        holder.getView(R.id.subImageView).setTag(imageItem);
        holder.getView(R.id.imgVideo).setTag(imageItem);
        holder.getView(R.id.videoPlay).setTag(imageItem);
    }

    @Override
    public void convert(ViewHolder holder, ImageItem imageItem, int position) {
        updateUI(holder, imageItem);
    }



    public void updateUI(ViewHolder holder, ImageItem imgData) {
        RoundProgressBar loadProgress = holder.getView(R.id.loadProgress);

        PhotoView imageView = holder.getView(R.id.subImageView);
//        imageView.setOnClickListener(v -> activity.toggleStateChange());

        View rvLayout = holder.getView(R.id.rvLayout);
        ImageView imgVideo = holder.getView(R.id.imgVideo);
//        imgVideo.setOnClickListener(v -> activity.toggleStateChange());
        ImageView videoPlay = holder.getView(R.id.videoPlay);

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
