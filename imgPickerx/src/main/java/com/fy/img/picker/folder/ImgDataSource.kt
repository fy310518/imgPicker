package com.fy.img.picker.folder

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.fy.baselibrary.utils.FileUtils
import com.fy.baselibrary.utils.notify.L
import com.fy.bean.ImageFolder
import com.fy.bean.ImageItem
import com.fy.img.picker.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * description TODO
 * Created by fangs on 2024/12/16 10:05.
 */
open class ImageDataSource : LoaderManager.LoaderCallbacks<Cursor> {

    private val IMAGE_PROJECTION = arrayOf( //查询图片需要的数据列
        MediaStore.Images.Media.DISPLAY_NAME,  //图片的显示名称  aaa.jpg
        MediaStore.Images.Media.DATA,  //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
        MediaStore.Images.Media.SIZE,  //图片的大小，long型  132492
        MediaStore.Images.Media.WIDTH,  //图片的宽度，int型  1920
        MediaStore.Images.Media.HEIGHT,  //图片的高度，int型  1080
        MediaStore.Images.Media.MIME_TYPE,  //图片的类型     image/jpeg
        MediaStore.Images.Media.DATE_ADDED,  //图片被添加的时间，long型  1450518608
        MediaStore.Images.Media.DURATION //视频、音频的时长，long型  132492
    )

    private var id = 0
    private var path: String? = null
    private var selectionArgsType = 0 // 查询类型：图片，video，img 和 video

    private var isInitLoad = false


    private var activity: FragmentActivity? = null
    private var loadedListener: OnImagesLoadedListener? = null //图片加载完成的回调接口
    private val imageFolders = ArrayList<ImageFolder>() //所有的图片文件夹

    private var imageFolder: ImageFolder? = null

    constructor(activity: AppCompatActivity,
        updateId: Boolean,
        selectionArgsType: Int,
        path: String?,
        imageFolder: ImageFolder?,
        loadedListener: OnImagesLoadedListener?) {
        this.activity = activity
        this.loadedListener = loadedListener
        this.path = path
        this.selectionArgsType = selectionArgsType

        if (null == imageFolder) {
            this.imageFolder = ImageFolder()
        } else {
            this.imageFolder = imageFolder
        }
        if (updateId) id++

        val loaderManager = LoaderManager.getInstance(activity)
        if (path == null) {
            loaderManager.initLoader(id, null, this) //加载所有的图片
        } else {
            //加载指定目录的图片
            val bundle = Bundle()
            bundle.putString("path", path)
            loaderManager.initLoader(id, bundle, this)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): CursorLoader {
        var selection = ""
        selection = if (selectionArgsType == 2) {
            MediaStore.Files.FileColumns.MIME_TYPE + " LIKE 'video%' OR " + MediaStore.Files.FileColumns.MIME_TYPE + " LIKE 'image%' "
        } else if (selectionArgsType == 1) {
            MediaStore.Files.FileColumns.MIME_TYPE + " LIKE 'video%' "
        } else {
            MediaStore.Files.FileColumns.MIME_TYPE + " LIKE 'image%' "
        }

        var cursorLoader: CursorLoader? = null
        activity?.let {
            cursorLoader = if (TextUtils.isEmpty(path)) {
                //扫描所有图片
                // url  MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                CursorLoader(
                    it, MediaStore.Files.getContentUri("external"), IMAGE_PROJECTION,
                    selection,
                    null,
                    IMAGE_PROJECTION[6] + " DESC"
                )
            } else { //扫描某个文件夹
                CursorLoader(
                    it, MediaStore.Files.getContentUri("external"), IMAGE_PROJECTION,
                    IMAGE_PROJECTION[1] + " like '%" + args!!.getString("path") + "%' AND " +
                            selection,
                    null,
                    IMAGE_PROJECTION[6] + " DESC"
                )
            }
        }
        return cursorLoader!!
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        L.e("loader", "--------")
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        GlobalScope.launch {
            imageFolders.clear()
            if (data != null && data.moveToFirst()) {
                val allImages: MutableList<ImageItem> = java.util.ArrayList() //所有图片的集合,不分文件夹
                do {
                    //查询数据
                    val imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
                    val imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
                    val imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))
                    val imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]))
                    val imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]))
                    val imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]))
                    val imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]))
                    val duration = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[7]))

                    //文件不存在 结束本次循环
                    if (!FileUtils.fileIsExist(imagePath) || imageWidth == 0 || imageSize <= 0) {
                        continue
                    }
                    //封装实体
                    val imageItem = ImageItem()
                    imageItem.name = imageName
                    imageItem.path = imagePath
                    imageItem.size = imageSize
                    imageItem.duration = duration
                    imageItem.setWidth(imageWidth.toString() + "")
                    imageItem.setHeight(imageHeight.toString() + "")
                    imageItem.mimeType = imageMimeType
                    imageItem.addTime = imageAddTime
                    if (imageFolder!!.images.contains(imageItem)) imageItem.isSelect = true //已选中的图片 包含当前图片 则设置当前图片为选择状态

                    allImages.add(imageItem)

                    //根据父路径分类存放图片
                    val imageFile = File(imagePath)
                    val imageParentFile = imageFile.parentFile
                    val imageFolder = ImageFolder()
                    imageFolder.name = imageParentFile.name
                    imageFolder.path = imageParentFile.absolutePath

                    if (!imageFolders.contains(imageFolder)) {
                        val images = ArrayList<ImageItem>()
                        images.add(imageItem)
                        imageFolder.cover = imageItem
                        imageFolder.images = images
                        imageFolders.add(imageFolder)
                    } else {
                        imageFolders[imageFolders.indexOf(imageFolder)].images.add(imageItem)
                    }
                } while (data.moveToNext())

                //全部图片
                if (data.count > 0 && allImages.size > 0) {

                    //构造所有图片的集合
                    val allImagesFolder = ImageFolder()
                    allImagesFolder.name = activity!!.resources.getString(R.string.all_images)
                    allImagesFolder.path = "/"
                    allImagesFolder.images = if (allImages.size > 100) allImages.subList(0, 100) else allImages
                    allImagesFolder.cover = if (allImages.size > 0) allImages[0] else ImageItem()
                    imageFolders.add(0, allImagesFolder) //确保第一条是所有图片
                }
            }

            withContext(Dispatchers.Main){
                if (!isInitLoad) { //是否第一次 加载
                    isInitLoad = true
                    loadedListener?.onImagesLoaded(imageFolders, true)
                } else if (imageFolders.size > 0) {
                    loadedListener?.onImagesLoaded(imageFolders, false)
                }
            }
        }
    }


    //重启加载器
    fun restartLoader(activity: AppCompatActivity, path: String?) {
        this.path = path
        LoaderManager.getInstance(activity).destroyLoader(id)
        val loaderManager = LoaderManager.getInstance(activity)
        if (path == null) {
            loaderManager.restartLoader(id, null, this)
        } else {
            //加载指定目录的图片
            val bundle = Bundle()
            bundle.putString("path", path)
            loaderManager.restartLoader(id, bundle, this)
        }
    }


    /** 所有图片加载完成的回调接口  */
    interface OnImagesLoadedListener {
        fun onImagesLoaded(imageFolders: List<ImageFolder>, isInitLoad: Boolean)
    }
}