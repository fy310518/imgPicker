package com.fy.bean;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 图片信息
 * Created by fangs on 2017/6/29.
 */
public class ImageItem implements Serializable, Comparable<ImageItem> {

    public ImageItem(String path) {
        this.path = path;
    }

    public String name;//图片的名字

    @Expose
    @SerializedName(value = "path", alternate = {"suspectImg", "url"})
    public String path;       //图片的路径

    private String md5 = "";

    public String width = "0";         //图片的宽度
    public String height = "0";        //图片的高度
    public long size = 0;     //图片的大小
    public String mimeType;   //图片的类型
    public long addTime;      //图片的创建时间
    public long duration;     //音视频的时长

    public boolean isSelect;  //是否选中

    private String suspectId;

    private int suspectType;
    private String  suspectName;

    public int bijiao;


    public String getSuspectName() {
        return suspectName == null ? "" : suspectName;
    }

    public void setSuspectName(String suspectName) {
        this.suspectName = suspectName;
    }

    public String getSuspectId() {
        return suspectId == null ? "" : suspectId;
    }

    public int getSuspectType() {
        return suspectType;
    }

    public void setSuspectType(int suspectType) {
        this.suspectType = suspectType;
    }

    public int getIsShowCamera() {
        return isShowCamera;
    }

    public int getBijiao() {
        return bijiao;
    }

    public void setBijiao(int bijiao) {
        this.bijiao = bijiao;
    }

    public String getSuspectUid() {
        return suspectId == null ? "" : suspectId;
    }

    public void setSuspectId(String suspectId) {
        this.suspectId = suspectId;
    }

    public int isShowCamera = 1;   //是否显示拍照按钮 1：表示不显示；0：显示

    public ImageItem() {
    }

    public ImageItem(int isShowCamera) {
        this.isShowCamera = isShowCamera;
    }

    public ImageItem(String path, boolean isSelect) {
        this.path = path;
        this.isSelect = isSelect;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path == null ? "" : path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMd5() {
        return md5 == null ? "" : md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getWidth() {
        return width == null ? "" : width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height == null ? "" : height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType == null ? "" : mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int isShowCamera() {
        return isShowCamera;
    }

    public void setIsShowCamera(int isShowCamera) {
        this.isShowCamera = isShowCamera;
    }

    /**
     * 图片的路径和创建时间相同就认为是同一张图片
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof ImageItem) {
            ImageItem item = (ImageItem) o;
            return this.path.equalsIgnoreCase(item.path) && this.addTime == item.addTime;
        }

        return super.equals(o);
    }

    @Override
    public int compareTo(@NonNull ImageItem o) {
        return o.getBijiao() - this.bijiao;
    }
}
