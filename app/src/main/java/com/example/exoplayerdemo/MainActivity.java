package com.example.exoplayerdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.video.VideoListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private View decorView;
    private SimpleExoPlayer player;
    private StyledPlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorView = getWindow().getDecorView();

        this.fullScreenMode();
    }

    // 全屏模式
    private void fullScreenMode() {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        this.getPermission();
    }

    // 获取所需权限
    private void getPermission() {
        setContentView(R.layout.activity_main);

        playerView = (StyledPlayerView) findViewById(R.id.player_view);

        //测试访问用户权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            /**
             * 可以同时询问开启两个权限
             * ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
             *Manifest.permission.ACCESS_FINE_LOCATION}, 1);
             */
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET},
                    1);
        } else {
            playVideo();
        }
    }

    // 播放视频
    private void playVideo() {
        player = new SimpleExoPlayer.Builder(this).build();

        String h = "https://vfx.mtime.cn/Video/2019/07/12/mp4/190712140656051701.mp4"; //需横屏
        String f = "file:///storage/emulated/0/DCIM/Camera/8437bdd7663961c4fadbc7f7f6e07acd.mp4"; //无需横屏
        List<String> list = new ArrayList<>();
        list.add(h);
        list.add(f);

        for(String vo:list){
            MediaItem item = MediaItem.fromUri(vo);
            player.addMediaItem(item);
        }

        player.addListener(new Player.EventListener() {
            @Override
            public void onIsLoadingChanged(boolean isLoading) {
                Log.d(TAG, "isLoading");
            }

            @Override
            public void onEvents(Player player, Player.Events events) {
                Log.d(TAG, "onEvents change");
            }

            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    Log.d(TAG, "onPlaybackStateChanged" + "就绪");

                }
                if (state == Player.STATE_ENDED) {
                    Log.d(TAG, "onPlaybackStateChanged" + "结束");
                }
            }

            @Override
            public void onMediaItemTransition(
                    @Nullable MediaItem mediaItem, @Player.MediaItemTransitionReason int reason) {
                Log.d(TAG, "onMediaItemTransition 播放下一个" + reason);
            }
        });

        player.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                Log.d(TAG, "onVideoSizeChanged" + width + "===>" + height + "===>" + unappliedRotationDegrees + "===>" + pixelWidthHeightRatio);

                if(width > height){
                    // 需要横屏播放
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
                }else{
                    // 无需横屏播放
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
                }

            }


        });

        // Build the media item.
//        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        // Set the media item to be played.
//        player.setMediaItem(mediaItem);

        playerView.setPlayer(player);

        // Prepare the player.
        player.setPlayWhenReady(true);
        player.prepare();
    }


    // 隐藏状态栏 及 导航栏
    private void hideStatusAndNavigator() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "你好啊，定时器运行");
//                fullScreenMode();
            }
        };
        timer.schedule(task, 2000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}