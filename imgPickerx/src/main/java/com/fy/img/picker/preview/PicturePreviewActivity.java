package com.fy.img.picker.preview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.fy.baselibrary.application.ioc.ConfigUtils;
import com.fy.baselibrary.application.mvvm.BaseViewModel;
import com.fy.baselibrary.application.mvvm.IBaseMVVM;
import com.fy.baselibrary.rv.CmRecyclerView;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.config.StatusBarUtils;
import com.fy.baselibrary.utils.notify.T;
import com.fy.bean.ImageFolder;
import com.fy.bean.ImageItem;
import com.fy.img.picker.PickerConfig;
import com.fy.img.picker.R;
import com.fy.img.picker.databinding.LayoutPreviewBinding;
import com.fy.img.picker.folder.ImageDataSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片预览activity
 * Created by fangs on 2017/7/6.
 */
public class PicturePreviewActivity extends AppCompatActivity implements IBaseMVVM<BaseViewModel, LayoutPreviewBinding>, View.OnClickListener {

    protected FrameLayout pickerBottom;         //底部布局
    protected ConstraintLayout rlHead;          //头部布局
    protected TextView tvTitle;                 //显示当前图片的位置  例如  5/31
    protected ImageView tvBack;
    protected TextView tvMenu;
    protected CmRecyclerView viewPager;
    protected CheckBox original_checkbox;
    protected TextView send;

    protected ImageFolder imgFolder;            //跳转进 PicturePreviewActivity 的图片文件夹
    protected List<ImageItem> selectedImages;   //所有已经选中的图片
    protected int mCurrentPosition = 0;         //跳转进PicturePreviewActivity时的序号，第几个图片; 当前显示的图片下标
    protected int max = 9;                     //最大选择图片数目
    private int selectionArgsType; //查询类型：图片，video，img 和 video

    /**
     * 当前屏幕状态 全屏or显示菜单
     */
    private int currentState = PickerConfig.STATE_SHOW_MENU;

    @Override
    public int setContentLayout() {
        return R.layout.layout_preview;
    }

    @Override
    public void initData(BaseViewModel baseViewModel, LayoutPreviewBinding layoutPreviewBinding, Bundle savedInstanceState) {
        StatusBarUtils.Companion.setStatusBarColor(this, ResUtils.getColor(R.color.black), ResUtils.getColor(R.color.black));

        rlHead = findViewById(R.id.rlHead);
        rlHead.setBackgroundColor(getResources().getColor(R.color.imgPreviewHeadBg));

        viewPager = findViewById(R.id.bannerViewPager);
        tvTitle = findViewById(R.id.tvTitle);
        tvBack = findViewById(R.id.tvBack);
        tvBack.setImageResource(ConfigUtils.getBackImg());
        tvBack.setOnClickListener(this);
        tvMenu = findViewById(R.id.tvMenu);

        original_checkbox = findViewById(R.id.original_checkbox);
        pickerBottom = findViewById(R.id.pickerBottom);
        pickerBottom.setOnClickListener(this);
        send = findViewById(R.id.send);
        send.setOnClickListener(this);
        original_checkbox.setOnClickListener(this);

        getTransmitData();
    }

    /**
     * 获取传递的数据
     */
    private void getTransmitData() {
        Bundle bundle = getIntent().getExtras();
        mCurrentPosition = bundle.getInt(PickerConfig.KEY_CURRENT_POSITION, 0);
        max = bundle.getInt(PickerConfig.KEY_MAX_COUNT, -1);
        selectionArgsType = bundle.getInt(PickerConfig.selectionArgsType, 0);

        ImageFolder folder = (ImageFolder) bundle.getSerializable(PickerConfig.KEY_ALREADY_SELECT);
        if (null != folder) selectedImages = folder.images;

        if (max < 0) pickerBottom.setVisibility(View.GONE);
        imgFolder = (ImageFolder) bundle.getSerializable(PickerConfig.KEY_IMG_FOLDER);
        if (null == imgFolder) {
            String imgFolderPath = bundle.getString(PickerConfig.KEY_FOLDER_PATH, "");
            if (!TextUtils.isEmpty(imgFolderPath)){
                File file = new File(imgFolderPath);
                initImgFolder(file.getParent(), folder);
            }
        } else {
            initPage();
        }
    }

    private void initPage(){
        if(imgFolder.images.size() == 0){
            return;
        }
        
        //初始化当前页面的状态
        tvTitle.setText(ResUtils.getReplaceStr(R.string.preview_image_count, mCurrentPosition + 1, imgFolder.images.size()));
        tvTitle.setTextColor(ResUtils.getColor(com.fy.baselibrary.R.color.white));
        original_checkbox.setChecked(imgFolder.images.get(mCurrentPosition).isSelect);
        tvMenu.setVisibility(View.INVISIBLE);

        viewPager.setFlingMaxVelocity(4000);
        viewPager.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(viewPager);

        LocalImageListAdapter adapter = new LocalImageListAdapter(this, imgFolder.images);
        viewPager.setAdapter(adapter);
        viewPager.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    try {
                        View view = snapHelper.findSnapView(viewPager.getLayoutManager());
                        mCurrentPosition = viewPager.getLayoutManager().getPosition(view);

                        original_checkbox.setChecked(imgFolder.images.get(mCurrentPosition).isSelect);
                        tvTitle.setText(ResUtils.getReplaceStr(R.string.preview_image_count, mCurrentPosition + 1, imgFolder.images.size()));
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
        viewPager.smoothScrollToPosition(mCurrentPosition);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.send) {//完成
            Bundle bundle = new Bundle();
            bundle.putSerializable("pickerImgData", imgFolder.images.get(mCurrentPosition));
            bundle.putSerializable(PickerConfig.imgFolderkey, new ImageFolder(selectedImages));
            JumpUtils.jumpResult(PicturePreviewActivity.this, bundle);
        } else if (i == R.id.original_checkbox) {
            if (original_checkbox.isChecked() && selectedImages.size() >= max) {
                T.show(ResUtils.getReplaceStr(R.string.select_limit, max), -1);
                original_checkbox.setChecked(false);
            } else {
                ImageItem imgItem = imgFolder.images.get(mCurrentPosition);
                if (original_checkbox.isChecked()) {
                    imgItem.setSelect(true);//设置状态 属性为 选中
                    selectedImages.add(imgItem);
                } else {
                    selectedImages.remove(imgItem);
                    imgItem.setSelect(false);//设置状态 属性为 未选中
                }
            }
        } else if (i == R.id.tvBack){
            JumpUtils.exitActivity(this);
        }
    }

    /**
     * 隐藏 或显示 标题栏，底部栏
     */
    public void toggleStateChange() {
        if (currentState == PickerConfig.STATE_SHOW_MENU) {
            currentState = PickerConfig.STATE_FULLSCREEN;
            rlHead.setVisibility(View.GONE);
            if(max != -1)pickerBottom.setVisibility(View.GONE);
        } else {
            currentState = PickerConfig.STATE_SHOW_MENU;
            rlHead.setVisibility(View.VISIBLE);
            if(max != -1)pickerBottom.setVisibility(View.VISIBLE);
        }
    }

    //初始化图片文件夹 相关
    private void initImgFolder(String imgFolderPath, ImageFolder folder) {
        new ImageDataSource(this, false, selectionArgsType, imgFolderPath, folder, new ImageDataSource.OnImagesLoadedListener() {
            @Override
            public void onImagesLoaded(@NonNull ArrayList<ImageFolder> imageFolders, boolean isInitLoad) {
                if (imageFolders.size() > 0 ){
                    for (ImageFolder imgFolderItem : imageFolders){
                        if (!TextUtils.isEmpty(imgFolderItem.path) && imgFolderPath.equals(imgFolderItem.path)){
                            imgFolder = imgFolderItem;
                            break;
                        }
                    }
                    if(null == imgFolder) imgFolder = new ImageFolder();

                    initPage();
                } else {
                    T.show(R.string.folderNoImage, -1);
                }
            }
        });
    }
}
