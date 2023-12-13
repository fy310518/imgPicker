package com.fy.img.picker;

/**
 * key
 * Created by fangs on 2017/7/7.
 */
public class PickerConfig {

    /** 要显示的图片文件夹 */
    public static final String KEY_IMG_FOLDER = "picture_imgFolder";
    /** 文件夹路径 */
    public static final String KEY_FOLDER_PATH = "picture_imgFolder_path";
    public static final String KEY_SELECTED = "picture_selected";
    /** 首先显示的图片 下标 */
    public static final String KEY_CURRENT_POSITION = "current_position";
    public static final String KEY_CURRENT_btnEnable = "current_btnEnable";
    /** 最多选择图片数目 key */
    public static final String KEY_MAX_COUNT = "max_count";
    /** 查询类型：图片，video，img 和 video key */
    public static final String selectionArgsType = "selectionArgsType";
    /** 是否显示拍照按钮 key */
    public static final String KEY_ISTAKE_picture = "take_a_picture";
    /** 是否可以预览大图 key */
    public static final String KEY_ISTAKE_canPreview = "canPreview";

    /** 已经选择的图片 */
    public static final String KEY_ALREADY_SELECT = "Already_SELECT";

    /** 全屏状态 */
    public static final int STATE_FULLSCREEN = 0;
    /** 显示菜单状态 */
    public static final int STATE_SHOW_MENU = 1;

    /** 单选模式 */
    public static final int SINGLE_MODE = 25535;
    /** 多选模式 */
    public static final int MULTIPLE_MODE = 25536;


    /**
     * 选择的图片文件夹 key
     */
    public static final String imgFolderkey = "ImageFolder";
    /**
     * 图片选择 请求码
     */
    public static final int Picture_Selection = 1001;
    /**
     * 图片预览 请求码
     */
    public static final int Picture_Preview = 10002;
    /**
     * 拍照 请求码
     */
    public static final int Photograph = 10003;
    /**
     * 图片裁剪 请求码
     */
    public static final int KJNova_Clipper = 10005;

}
