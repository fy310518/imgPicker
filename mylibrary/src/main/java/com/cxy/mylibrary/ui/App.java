package com.cxy.mylibrary.ui;

import android.app.Application;

import com.cxy.mylibrary.R;
import com.fy.baselibrary.application.BaseActivityLifecycleCallbacks;
import com.fy.baselibrary.application.ioc.ConfigUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.ScreenUtils;

/**
 * description </p>
 * Created by fangs on 2021/5/26 15:42.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new ConfigUtils.ConfigBiuder()
                .setBgColor(R.color.appHeadBg)
                .setTitleColor(R.color.white)
                .setTitleCenter(true)
                .setFontDefault(false)
                .setBaseFile("cxyIM", 0)
                .setEnableCacheInterceptor(true)
                .setBASE_URL("https://www.wanandroid.com/")
                .create(getApplicationContext());

        initConfig();
    }


    private void initConfig() {
        int designWidth = (int) ResUtils.getMetaData(this, "rudeness_Adapter_Screen_width", 0);
        ScreenUtils.setCustomDensity(this, designWidth);

//        设置activity 生命周期回调
        registerActivityLifecycleCallbacks(new BaseActivityLifecycleCallbacks());
    }
}
