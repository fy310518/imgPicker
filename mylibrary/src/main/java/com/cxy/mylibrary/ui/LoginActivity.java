package com.cxy.mylibrary.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cxy.mylibrary.R;
import com.fy.baselibrary.utils.AppUtils;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.img.picker.PickerConfig;
import com.fy.img.picker.view.RoundProgressBar;

/**
 * description </p>
 * Created by fangs on 2021/5/26 14:34.
 */
public class LoginActivity extends AppCompatActivity {

    RoundProgressBar loadProgress;
    int mProgress = 7;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadProgress = findViewById(R.id.loadProgress);


        loadProgress.setProgress(mProgress);
        loadProgress.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadProgress.setProgress(mProgress += 8);
            }
        }, 500);

        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(PickerConfig.KEY_ISTAKE_picture, true);
                bundle.putInt(PickerConfig.KEY_MAX_COUNT, 9);
                JumpUtils.jump(LoginActivity.this, AppUtils.getLocalPackageName() + ".picker.ImgPickerActivity", bundle, PickerConfig.Picture_Selection);
            }
        });
    }
}
