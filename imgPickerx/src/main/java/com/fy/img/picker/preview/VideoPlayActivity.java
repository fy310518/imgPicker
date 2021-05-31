package com.fy.img.picker.preview;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.img.picker.databinding.ActVideoPalyBinding;

/**
 * description 简单视频播放界面
 * Created by fangs on 2021/5/31 10:57.
 */
public class VideoPlayActivity extends AppCompatActivity implements IBaseActivity {

    ActVideoPalyBinding viewBinding;

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public ActVideoPalyBinding getView() {
        viewBinding = ActVideoPalyBinding.inflate(LayoutInflater.from(this));
        return viewBinding;
    }

    @StatusBar(statusOrNavModel = 1, statusStrColor = "black", navStrColor = "black")
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        String videoPath = getIntent().getExtras().getString("videoPath", "");

        viewBinding.videoView.setVideoPath(videoPath);

        //创建MediaController对象
        MediaController mediaController = new MediaController(this);
        mediaController.setMediaPlayer(viewBinding.videoView);

        //VideoView与MediaController建立关联
        viewBinding.videoView.setMediaController(mediaController);

        //让VideoView获取焦点
        viewBinding.videoView.requestFocus();

        mediaController.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewBinding.videoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewBinding.videoView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewBinding.videoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewBinding.videoView.destroyDrawingCache();
    }
}
