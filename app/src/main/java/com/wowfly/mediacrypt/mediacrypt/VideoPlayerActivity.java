package com.wowfly.mediacrypt.mediacrypt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by user on 9/12/14.
 */
public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {
    private static final String TAG = "VideoPlayerActivity";
    SurfaceView videoSurface;
    MediaPlayer player;
    VideoControllerView controller;

    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.activity_video_player);

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);

        player = new MediaPlayer();
        controller = new VideoControllerView(this);

        Intent i = getIntent();
        String uri = i.getStringExtra("uri");
        Log.i(TAG, " uri " + uri);

/*        do {
            Log.i(TAG, " waiting for http server startup");
            try {
                Thread.sleep(200);
            }catch (InterruptedException e) {
                Log.i(TAG, " sleep " + e.getMessage());
            }
        } while(mVSD.wasStarted() == false);*/


        try {
            player.setDataSource(this, Uri.parse("/storage/sdcard0/cartoon.mp4"));

            //player.setDataSource("http://192.168.1.115:8181/dev/ssl-aes/cartoon.mp4");
            //player.setDataS
            //player.setDataSource("http://127.0.0.1:10086/video");
            player.setOnPreparedListener(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        controller.setMediaPlayer(null);
        player.stop();
        super.onDestroy();

    }
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        player.prepareAsync();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        player.start();
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    public int getBufferPercentage() {
        return 0;
    }

    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    public int getDuration() {
        return player.getDuration();
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }
    public void pause() {
        player.pause();
    }

    public void seekTo(int i) {
        player.seekTo(i);
    }

    public void start() {
        player.start();
    }
    public boolean isFullScreen() {
        return true;
    }
    public void toggleFullScreen() {

    }
}