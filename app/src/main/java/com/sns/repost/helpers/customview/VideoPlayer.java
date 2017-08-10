package com.sns.repost.helpers.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;


import com.sns.repost.helpers.callback.SuccessfullCallback;
import com.sns.repost.models.Media;

import java.io.IOException;

/**
 * Created by Hien on 5/6/2017.
 */

public class VideoPlayer extends TextureView implements TextureView.SurfaceTextureListener {
    private static String TAG = "VideoPlayer";

    /**
     * This flag determines that if current VideoPlayer object is first item of the list if it is first item of list
     */
    boolean isFirstListItem;

    boolean isLoaded;
    public boolean isMpPrepared;

    Media video;
    String url;
    MediaPlayer mp;
    Surface surface;
    SurfaceTexture s;

    int mWidth = 0;
    int mHeight = 0;

    SuccessfullCallback callback;

    public void setAvailableCallBack(SuccessfullCallback callBack) {
        this.callback = callBack;
    }

    public VideoPlayer(Context context) {
        super(context);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void loadVideo(String localPath, Media video) {

        this.url = localPath;
        this.video = video;
        isLoaded = true;

        if (this.isAvailable()) {
            prepareVideo(getSurfaceTexture());
        }

        setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
        isMpPrepared = false;
        prepareVideo(surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    protected void onDetachedFromWindow() {
        // release resources on detach
        if (mp != null) {
            mp.release();
            mp = null;
        }
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        surface.release();
        if (mp != null) {
            mp.stop();
            mp.reset();
            mp.release();
            mp = null;
        }

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    public void prepareVideo(SurfaceTexture t) {

        this.surface = new Surface(t);
        mp = new MediaPlayer();
        mp.setVolume(0, 0);
        mp.setSurface(this.surface);
       // VideoPlayer.this.setLayoutParams(new RelativeLayout.LayoutParams(0,0));
        try {
            mp.setDataSource(url);
            mp.prepareAsync();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    isMpPrepared = true;
                    mp.setLooping(true);
                    //VideoPlayer.this.setLayoutParams(new RelativeLayout.LayoutParams(getmWidth(),getmHeight()));
                    //iVideoPreparedListener.onVideoPrepared(video);
                    //mp.start();
                    callback.success();
                }


            });
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        try {

        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    public boolean startPlay() {
        if (mp != null)
            if (!mp.isPlaying()) {
                mp.start();
                return true;
            }

        return false;
    }

    public void pausePlay() {
        if (mp != null)
            mp.pause();
    }

    public void stopPlay() {
        if (mp != null)
            mp.stop();
    }

    public void changePlayState() {
        if (mp != null) {
            if (mp.isPlaying())
                mp.pause();
            else
                mp.start();
        }

    }

    public void playVideo() {
        if (mp != null) {
            if (mp.isPlaying()) {
            } else
                mp.start();
        }

    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public int getmWidth() {
        return mWidth;
    }

    public int getmHeight() {
        return mHeight;
    }
}
