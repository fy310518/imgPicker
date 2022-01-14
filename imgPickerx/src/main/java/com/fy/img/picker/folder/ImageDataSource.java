package com.fy.img.picker.folder;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.notify.L;
import com.fy.bean.ImageFolder;
import com.fy.bean.ImageItem;
import com.fy.img.picker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 加载手机图片实现类
 * Created by fangs on 2017/6/30.
 */
public class ImageDataSource implements LoaderManager.LoaderCallbacks<Cursor> {
    private int id = 0;
    private String path;

    private boolean isInitLoad;

    private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED,     //图片被添加的时间，long型  1450518608
            MediaStore.Images.Media.DURATION        //视频、音频的时长，long型  132492
    };

    private FragmentActivity activity;
    private OnImagesLoadedListener loadedListener;                     //图片加载完成的回调接口
    private List<ImageFolder> imageFolders = new ArrayList<>();   //所有的图片文件夹
    private ImageFolder imageFolder;                              //已选中的图片

    /**
     * @param activity       用于初始化LoaderManager，需要兼容到2.3
     * @param path           指定扫描的文件夹目录，可以为 null，表示扫描所有图片
     * @param loadedListener 图片加载完成的监听
     */
    public ImageDataSource(AppCompatActivity activity, boolean updateId, String path, ImageFolder imageFolder,
                           OnImagesLoadedListener loadedListener) {
        this.activity = activity;
        this.imageFolder = imageFolder;
        this.loadedListener = loadedListener;
        this.path = path;
        if (updateId) id++;

        LoaderManager loaderManager = LoaderManager.getInstance(activity);
        if (path == null) {
            loaderManager.initLoader(id, null, this);//加载所有的图片
        } else {
            //加载指定目录的图片
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            loaderManager.initLoader(id, bundle, this);
        }
    }

    //重启加载器
    public void restartLoader(AppCompatActivity activity, String path){
        this.path = path;
        LoaderManager.getInstance(activity).destroyLoader(id);
        LoaderManager loaderManager = LoaderManager.getInstance(activity);

        if (path == null) {
            loaderManager.restartLoader(id, null, this);
        } else {
            //加载指定目录的图片
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            loaderManager.restartLoader(id, bundle, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;

        if (TextUtils.isEmpty(path)) {
            //扫描所有图片
            // url  MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            cursorLoader = new CursorLoader(activity, MediaStore.Files.getContentUri("external"), IMAGE_PROJECTION,
                    MediaStore.Files.FileColumns.MIME_TYPE + "= ? OR " + MediaStore.Files.FileColumns.MIME_TYPE  + "= ?",
                    new String[]{"video/mp4", "image/jpeg"},
                    IMAGE_PROJECTION[6] + " DESC");
        }

        else { //扫描某个文件夹
            cursorLoader = new CursorLoader(activity, MediaStore.Files.getContentUri("external"), IMAGE_PROJECTION,
                    IMAGE_PROJECTION[1] + " like '%" + args.getString("path") + "%'" +
                            MediaStore.Files.FileColumns.MIME_TYPE + "= ? OR " + MediaStore.Files.FileColumns.MIME_TYPE  + "= ?",
                    new String[]{"video/mp4", "image/jpeg"},
                    IMAGE_PROJECTION[6] + " DESC");
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        imageFolders.clear();
        if (data != null && data.moveToFirst()) {
            List<ImageItem> allImages = new ArrayList<>();   //所有图片的集合,不分文件夹
            do{
                //查询数据
                String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                long imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                int imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                int imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                String imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                long imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
                long duration = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[7]));

                //文件不存在 结束本次循环
                if (!FileUtils.fileIsExist(imagePath) || imageSize <= 0 || TextUtils.isEmpty(imageMimeType)
                     || (!imageMimeType.equals("image/jpeg") && !imageMimeType.equals("video/mp4")) ){
                    continue;
                }
                //封装实体
                ImageItem imageItem = new ImageItem();
                imageItem.name = imageName;
                imageItem.path = imagePath;
                imageItem.size = imageSize;
                imageItem.duration = duration;
                imageItem.setWidth(imageWidth + "");
                imageItem.setHeight(imageHeight + "");
                imageItem.mimeType = imageMimeType;
                imageItem.addTime = imageAddTime;
                if (imageFolder.images.contains(imageItem))imageItem.isSelect = true;//已选中的图片 包含当前图片 则设置当前图片为选择状态

                allImages.add(imageItem);
                //根据父路径分类存放图片
                File imageFile = new File(imagePath);
                File imageParentFile = imageFile.getParentFile();
                ImageFolder imageFolder = new ImageFolder();
                imageFolder.name = imageParentFile.getName();
                imageFolder.path = imageParentFile.getAbsolutePath();

                if (!imageFolders.contains(imageFolder)) {
                    ArrayList<ImageItem> images = new ArrayList<>();
                    images.add(imageItem);
                    imageFolder.cover = imageItem;
                    imageFolder.images = images;
                    imageFolders.add(imageFolder);
                } else {
                    imageFolders.get(imageFolders.indexOf(imageFolder)).images.add(imageItem);
                }
            } while (data.moveToNext());

            //全部图片
            if (data.getCount() > 0 && allImages.size() > 0) {

                //构造所有图片的集合
                ImageFolder allImagesFolder = new ImageFolder();
                allImagesFolder.name = activity.getResources().getString(R.string.all_images);
                allImagesFolder.path = "/";
                allImagesFolder.images =  allImages.size() > 100 ? allImages.subList(0, 100) : allImages;
                allImagesFolder.cover = allImages.size() > 0 ? allImages.get(0) : new ImageItem();
                imageFolders.add(0, allImagesFolder);  //确保第一条是所有图片
            }
        }

        if (!isInitLoad) {//是否第一次 加载
            isInitLoad = true;
            loadedListener.onImagesLoaded(imageFolders, true);
        } else if (imageFolders.size() > 0){
            loadedListener.onImagesLoaded(imageFolders, false);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        L.e("loader", "--------");
    }

    /** 所有图片加载完成的回调接口 */
    public interface OnImagesLoadedListener {
        void onImagesLoaded(List<ImageFolder> imageFolders, boolean isInitLoad);
    }
}
