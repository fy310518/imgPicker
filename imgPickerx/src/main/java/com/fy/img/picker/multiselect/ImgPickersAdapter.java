package com.fy.img.picker.multiselect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.rv.adapter.MultiCommonAdapter;
import com.fy.baselibrary.rv.adapter.MultiTypeSupport;
import com.fy.baselibrary.utils.PhotoUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.ScreenUtils;
import com.fy.baselibrary.utils.TimeUtils;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.fy.baselibrary.utils.notify.T;
import com.fy.bean.ImageFolder;
import com.fy.bean.ImageItem;
import com.fy.img.picker.PickerConfig;
import com.fy.img.picker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择（单选、多选） RecyclerView 适配器
 * Created by fangs on 2017/8/7.
 */
public class ImgPickersAdapter extends MultiCommonAdapter<ImageItem, ViewHolder> {

    public static String newFile = "";//最新的拍照图片路径

    private int mImageSize;
    protected List<ImageItem> selectedImages;   //所有已经选中的图片
    private OnImageItemClickListener listener;   //图片被点击的监听
    private OnCheckClickListener clickListener;  //checkBox点击监听
    private int selectLimit;

    private ImgPickersAdapter(Context context, List<ImageItem> datas) {
        super(context, datas, new MultiTypeSupport<ImageItem>(){
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == 1) {
                    return R.layout.adapter_image;
                } else {
                    return R.layout.adapter_camera;
                }
            }

            @Override
            public int getItemViewType(int position, ImageItem imageItem) {
                return imageItem.getIsShowCamera();
            }
        });

        mImageSize = ScreenUtils.getImageItemWidth();
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void convert(ViewHolder holder, ImageItem imgItem, int position) {
        if (position == 0 && imgItem.getIsShowCamera() == 0){//判断 是否显示 拍照按钮

            FrameLayout camera = holder.getView(R.id.camera);
            camera.setLayoutParams(new FrameLayout.LayoutParams(mImageSize, mImageSize)); //让图片是个正方形
            holder.setOnClickListener(R.id.camera, v -> {
                //拍照
                Intent intent = PhotoUtils.takePicture((Activity) mContext);
                newFile = intent.getStringExtra("newFilePath");
                ((Activity) mContext).startActivityForResult(intent, PickerConfig.Photograph);
            });
        } else {
            holder.setVisibility(R.id.txtVideoFlag, imgItem.mimeType.equals("video/mp4"));
            holder.setText(R.id.txtVideoFlag, TimeUtils.getTime((int) (imgItem.duration * 0.001)));

            ImageView ivThumb = holder.getView(R.id.iv_thumb);
            ivThumb.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
            ImgLoadUtils.loadImage(imgItem.path, R.mipmap.default_image, ivThumb);
            ivThumb.setOnClickListener(v -> {
                if (listener != null) listener.onImageItemClick(imgItem, position);
            });

            CheckBox cbCheck = holder.getView(R.id.cb_check);
            if (selectLimit < 1){
                cbCheck.setVisibility(View.GONE);
            } else {
                if (imgItem.isSelect) {
                    cbCheck.setChecked(true);
                } else {
                    cbCheck.setChecked(false);
                }

                cbCheck.setOnClickListener(v -> {
                    if (cbCheck.isChecked() && selectedImages.size() >= selectLimit) {
                        T.show(ResUtils.getReplaceStr(R.string.select_limit, selectLimit), -1);
                        cbCheck.setChecked(false);
                    } else {
                        if (cbCheck.isChecked()) {
                            if (!selectedImages.contains(imgItem))selectedImages.add(imgItem);
                            imgItem.setSelect(true);//设置状态 属性为 选中
                        } else {
                            selectedImages.remove(imgItem);
                            imgItem.setSelect(false);//设置状态 属性为 未选中
                        }
                    }

                    if (null != clickListener)clickListener.onClick(selectedImages.size());
                });
            }
        }
    }


    public void refreshData(List<ImageItem> images) {
        if (null == images || images.size() == 0){
            setmDatas(new ArrayList<>());
        } else {
            setmDatas(images);
        }

        notifyDataSetChanged();
    }

    public void setOnImageItemClickListener(OnImageItemClickListener listener) {
        this.listener = listener;
    }

    public void setClickListener(OnCheckClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setSelectLimit(int selectLimit) {
        this.selectLimit = selectLimit;
    }

    public List<ImageItem> getSelectedImages() {
        return selectedImages;
    }

    public void setSelectedImages(List<ImageItem> selectedImages) {
        this.selectedImages = selectedImages;
    }

    /**
     * 定义item 图片点击事件回调接口
     */
    public interface OnImageItemClickListener {
        void onImageItemClick(ImageItem imageItem, int position);
    }

    /**
     * 定义 item checkBox点击事件回调接口
     */
    interface OnCheckClickListener{
        void onClick(int num);
    }


    static class Builder{
        private int selectLimit;
        ImageFolder imageFolder;

        OnImageItemClickListener listener;
        OnCheckClickListener clickListener;

        public Builder() {}

        public Builder setSelectLimit(int selectLimit) {
            this.selectLimit = selectLimit;
            return this;
        }

        public Builder setImageFolder(ImageFolder imageFolder) {
            this.imageFolder = imageFolder;
            return this;
        }

        public Builder setOnImageItemClickListener(OnImageItemClickListener listener){
            this.listener = listener;
            return this;
        }

        public Builder setClickListener(OnCheckClickListener clickListener) {
            this.clickListener = clickListener;
            return this;
        }

        public ImgPickersAdapter create(Context context, List<ImageItem> datas){
            ImgPickersAdapter adapter = new ImgPickersAdapter(context, datas);
            adapter.setSelectLimit(selectLimit);
            adapter.setSelectedImages(imageFolder.images);
            adapter.setOnImageItemClickListener(listener);
            adapter.setClickListener(clickListener);

            return adapter;
        }
    }

}
