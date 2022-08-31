package com.fy.img.picker.multiselect;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fy.baselibrary.aop.annotation.NeedPermission;
import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.mvvm.BaseViewModel;
import com.fy.baselibrary.application.mvvm.IBaseMVVM;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.popupwindow.CommonPopupWindow;
import com.fy.baselibrary.base.popupwindow.NicePopup;
import com.fy.baselibrary.rv.divider.GridItemDecoration;
import com.fy.baselibrary.utils.DensityUtils;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.media.MediaScanner;
import com.fy.baselibrary.utils.media.UpdateMedia;
import com.fy.baselibrary.utils.notify.T;
import com.fy.bean.ImageFolder;
import com.fy.bean.ImageItem;
import com.fy.img.picker.PickerConfig;
import com.fy.img.picker.R;
import com.fy.img.picker.databinding.ActImgPickerBinding;
import com.fy.img.picker.folder.ImageDataSource;
import com.fy.img.picker.folder.ImageFolderAdapter;
import com.fy.img.picker.preview.PicturePreviewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择（单选、多选）
 * Created by fangs on 2017/6/29.
 */
public class ImgPickerActivity extends AppCompatActivity implements IBaseMVVM<BaseViewModel, ActImgPickerBinding>, View.OnClickListener {
    private ActImgPickerBinding vdb;

    protected TextView tvTitle;                 //显示当前图片的位置  例如  5/31
    protected TextView tvBack;
    protected TextView tvMenu;

    private ImageDataSource imageDataSource;
    private int selectionArgsType; //查询类型：图片，video，img 和 video
    private int maxCount = 9;//最大选择数目
    private boolean isTAKE_picture;//是否显示拍照 按钮
    private boolean canPreview;//是否可以预览大图
    private Button btn_dir;//全部图片
    private Button btn_complete;//完成

    private ImageFolder imgFolder;//当前界面显示的图片 文件夹
    private RecyclerView recycler;
    private ImgPickersAdapter mImgListAdapter;

    private CommonPopupWindow popupWindow;//图片文件夹的 弹窗
    private List<ImageFolder> imageFolderArray = new ArrayList<>();
    private int mPosition = 0;//弹窗选中

    @Override
    public int setContentLayout() {
        return R.layout.act_img_picker;
    }

    @StatusBar(statusStrColor = "statusBar", navStrColor = "navBar")
    @Override
    public void initData(BaseViewModel baseViewModel, ActImgPickerBinding actImgPickerBinding, Bundle savedInstanceState) {
        vdb = actImgPickerBinding;

        Bundle bundle = getIntent().getExtras();
        isTAKE_picture = bundle.getBoolean(PickerConfig.KEY_ISTAKE_picture, false);
        canPreview = bundle.getBoolean(PickerConfig.KEY_ISTAKE_canPreview, false);
        maxCount = bundle.getInt(PickerConfig.KEY_MAX_COUNT, -1);
        selectionArgsType = bundle.getInt(PickerConfig.selectionArgsType, 0);

        ImageFolder imageFolder = (ImageFolder) bundle.getSerializable(PickerConfig.KEY_ALREADY_SELECT);

        if (null == imageFolder) imageFolder = new ImageFolder(new ArrayList<>());
        initView(imageFolder);
        initRV(imageFolder);
        initImgFolder(imageFolder, false);
    }

    @NeedPermission({Manifest.permission.CAMERA})
    private void initView(ImageFolder imageFolder) {
        tvTitle = findViewById(R.id.tvTitle);
        tvBack = findViewById(R.id.tvBack);
        tvMenu = findViewById(R.id.tvMenu);
        tvBack.setOnClickListener(this);
        tvMenu.setOnClickListener(this);

        btn_dir = findViewById(R.id.btn_dir);
        btn_complete = findViewById(R.id.btn_complete);
        btn_dir.setOnClickListener(this);
        btn_complete.setOnClickListener(this);

        tvTitle.setText(R.string.select_img);
        tvMenu.setTextColor(Color.GRAY);
        tvMenu.setText(R.string.preview);
        if (maxCount == -1) tvMenu.setVisibility(View.GONE);//图片选择数目 小于1 隐藏 菜单按钮

        setViewStutas(imageFolder.images.size());
    }


    @SuppressLint("StringFormatMatches")
    private void initRV(ImageFolder imageFolder) {
        recycler = findViewById(R.id.recycler);

        //设置布局管理器
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(
                GridItemDecoration.Builder.init()
                        .setColumn(3)
                        .create(this));

        //设置adapter
        mImgListAdapter = new ImgPickersAdapter.Builder()
                .setSelectLimit(maxCount)
                .setImageFolder(imageFolder)
                .setClickListener(num -> setViewStutas(num))
                .setOnImageItemClickListener((imageItem, position) -> {
                    if (isTAKE_picture && mPosition == 0) { //是否显示拍照 按钮
                        List<ImageItem> images = new ArrayList<>();
                        images.addAll(imgFolder.images);
                        images.remove(0);
                        ImageFolder imgFol = new ImageFolder(images);
                        previewPicture(position - 1, maxCount, imgFol);
                    } else {
                        previewPicture(position, maxCount, imgFolder);
                    }
                })
                .create(this, new ArrayList<>());
        recycler.setAdapter(mImgListAdapter);
    }

    //初始化图片文件夹 相关
    private void initImgFolder(ImageFolder imageFolder, boolean updateId) {
        imageDataSource = new ImageDataSource(this, updateId, selectionArgsType,null, imageFolder, new ImageDataSource.OnImagesLoadedListener() {
            @Override
            public void onImagesLoaded(List<ImageFolder> imageFolders, boolean isInitLoad) {
                if (imageFolders.size() == 0 && isInitLoad) {
                    List<ImageItem> images = new ArrayList<>();
                    if (isTAKE_picture) {
                        images.add(new ImageItem(0));
                    }
                    mImgListAdapter.refreshData(images);
                    return;
                } else {
                    if (isTAKE_picture) {
                        List<ImageItem> selectedData = mImgListAdapter.getSelectedImages();
                        for (ImageFolder folderItem : imageFolders) {
                            if (folderItem.images.size() > 0){
//                                ImageItem imageItem = folderItem.images.get(0);
                                //使 刚刚拍照的图片为选中状态
//                                String filePath = SpfAgent.init("").getString(PickerConfig.newFilePath);
//                                if (imageItem.path.equals(filePath) && selectedData.size() < maxCount) {
//                                    imageItem.isSelect = true;
//                                    selectedData.add(imageItem);
//                                    setViewStutas(selectedData.size());
//                                }
//                                new SpfAgent(Constant.baseSpf).remove(PickerConfig.newFilePath, false);
                            }

                            folderItem.images.add(0, new ImageItem(0));//添加拍照按钮
                            break;
                        }
                    }

                    if (null != imgFolder){//当前显示的图片文件夹
                        for (ImageFolder iFolder : imageFolders){
                            if (iFolder.path.equals(imgFolder.path)){
                                imgFolder = iFolder;
                                break;
                            }
                        }
                    } else {
                        imgFolder = imageFolders.get(0);
                    }
                    mImgListAdapter.refreshData(imgFolder.images);
                }

                if (imageFolders.size() > 0){
                    imageFolderArray.clear();//图片文件夹 列表
                    imageFolderArray.addAll(imageFolders);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_dir) {//全部图片 按钮
            if (null != popupWindow && popupWindow.isShowing()) return;
            //计算popupWindow 高度（listview Item数量  * （Item高度 + 分割线高度））
            int itemCount = imageFolderArray.size() > 3 ? 3 : imageFolderArray.size();
            int pw_lv_height = 81 * itemCount;

            popupWindow = NicePopup.Builder.init()
                    .setLayoutId(R.layout.pop_imgfolder)
                    .setConvertListener(new NicePopup.PopupConvertListener(){
                        @Override
                        public void convertView(ViewHolder holder, CommonPopupWindow popupWindow) {
                            ImageFolderAdapter mImageFolderAdapter = new ImageFolderAdapter(ImgPickerActivity.this, imageFolderArray);
                            mImageFolderAdapter.setSelectIndex(mPosition, isTAKE_picture);

                            ListView lvImaFolder = holder.getView(R.id.lvImaFolder);
                            lvImaFolder.setAdapter(mImageFolderAdapter);

                            lvImaFolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    imgFolder = (ImageFolder) view.getTag();

                                    if (null != imgFolder) {
                                        T.show(imgFolder.name, -1);

                                        mImgListAdapter.refreshData(imgFolder.images);
                                        btn_dir.setText(imgFolder.name);

                                        ImgPickerActivity.this.mPosition = position;
                                        mImageFolderAdapter.setSelectIndex(position, isTAKE_picture);
                                    }

                                    popupWindow.dismiss();
                                }
                            });
                        }
                    }).create()
                    .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, pw_lv_height)
                    .setAnim(R.style.AnimDown)
                    .onCreateView(ImgPickerActivity.this);

            //得到button的左上角坐标
            int[] positions = new int[2];
            view.getLocationOnScreen(positions);
            popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.NO_GRAVITY,
                    positions[0],
                    positions[1] - DensityUtils.dp2px(pw_lv_height));

        } else if (i == R.id.btn_complete) {//完成
            if (mImgListAdapter.getSelectedImages().size() == 0) {
                return;
            }
            List<ImageItem> selectedData = mImgListAdapter.getSelectedImages();
            Bundle bundle = new Bundle();
            bundle.putSerializable(PickerConfig.imgFolderkey, new ImageFolder(selectedData));

            JumpUtils.jumpResult(this, bundle);

        } else if (i == R.id.tvMenu) {//预览
            if (mImgListAdapter.getSelectedImages().size() == 0) {
                return;
            }
            List<ImageItem> selectedData = mImgListAdapter.getSelectedImages();
            previewPicture(0, maxCount, new ImageFolder(selectedData));
        } else if (i == R.id.tvBack) {
            JumpUtils.exitActivity(this);
        }
    }

    /**
     * 设置 预览 按钮 和 完成按钮 内容 和状态
     *
     * @param num
     */
    public void setViewStutas(int num) {
        if (num > 0) {
            btn_complete.setTextColor(Color.WHITE);
            tvMenu.setEnabled(true);
            tvMenu.setTextColor(Color.WHITE);
            tvMenu.setText(ResUtils.getReplaceStr(R.string.preview_count, num));
        } else {
            btn_complete.setTextColor(Color.GRAY);
            tvMenu.setTextColor(Color.GRAY);
            tvMenu.setEnabled(false);
            tvMenu.setText(R.string.preview);
        }
    }

    /**
     * 预览大图
     *
     * @param position  首先显示的图片
     * @param maxCount  最多选择图片数目
     * @param imgFolder 要显示的图片文件夹
     */
    @SuppressLint("RestrictedApi")
    private void previewPicture(int position, int maxCount, ImageFolder imgFolder) {
        if (!canPreview) return;

        Bundle bundle = new Bundle();
        bundle.putInt(PickerConfig.KEY_MAX_COUNT, maxCount);
        bundle.putInt(PickerConfig.KEY_CURRENT_POSITION, position);

        if (null != imgFolder.images && imgFolder.images.size() > 100){
            bundle.putString(PickerConfig.KEY_FOLDER_PATH, imgFolder.images.get(0).path);//此处传递 一个路径
        } else {
            bundle.putSerializable(PickerConfig.KEY_IMG_FOLDER, imgFolder);
        }
        bundle.putSerializable(PickerConfig.KEY_ALREADY_SELECT, new ImageFolder(mImgListAdapter.getSelectedImages()));

        JumpUtils.jump(this, PicturePreviewActivity.class, bundle, PickerConfig.Picture_Preview);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PickerConfig.Picture_Preview:
                    if (null != data) JumpUtils.jumpResult(this, data.getExtras());
                    break;
                case PickerConfig.Photograph:
                    UpdateMedia.scanMedia(this, ImgPickersAdapter.newFile, new MediaScanner.OnMediaScannerCompleted(){
                        @Override
                        public void onScanCompleted() {
                            imageDataSource.restartLoader(ImgPickerActivity.this, null);
                        }
                    });
                    break;
            }
        }
    }

}
