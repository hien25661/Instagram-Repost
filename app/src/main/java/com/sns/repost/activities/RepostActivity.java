package com.sns.repost.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sns.repost.BaseActivity;
import com.sns.repost.R;
import com.sns.repost.api.SampleSaver;
import com.sns.repost.helpers.callback.SuccessfullCallback;
import com.sns.repost.helpers.customview.VideoPlayer;
import com.sns.repost.models.Media;
import com.sns.repost.utils.Consts;
import com.sns.repost.utils.StringUtils;
import com.sns.repost.utils.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nguyenvanhien on 8/10/17.
 */

public class RepostActivity extends BaseActivity {
    @Bind(R.id.media_image)
    ImageView mediaImage;
    @Bind(R.id.media_video)
    VideoPlayer mediaVideo;
    @Bind(R.id.imvAvatar)
    ImageView imvAvatar;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.tvTime)
    TextView tvTime;
    private Media media;
    boolean mSaveSate;
    String mSavedPath;
    Bitmap mBitmapResult, mBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repost);
        ButterKnife.bind(this);
        media = Utils.getMediaBundle(getIntent().getStringExtra(Consts.PARAM_MEDIA));
        loadData();
    }

    private void loadData() {
        if (media == null) return;
        Utils.showImage(this, media.getUser().getProfilePicture(), imvAvatar);
        tvUserName.setText(media.getUser().getUsername());
        tvTime.setText(Utils.getFormatTime(Long.parseLong(media.getCreated_time()) * 1000));
        if (media.getImages() != null && StringUtils.isNotEmpty(media.getImages().getStandardResolution().getUrl())) {
            Picasso.with(this).load(media.getImages().getStandardResolution().getUrl())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(mBackgroundTarget);
        }

        if (StringUtils.isNotEmpty(media.getType())) {
            if (media.getVideos() != null && media.getVideos().getStandardResolution() != null
                    && StringUtils.isNotEmpty(media.getVideos().getStandardResolution().getUrl())) {
                int videoNativeWidth = media.getVideos().getStandardResolution().getWidth();
                int videoNativeHeight = media.getVideos().getStandardResolution().getHeight();
                int screenWidth = Utils.getScreenWidth();
                if (videoNativeWidth != 0 && videoNativeHeight != 0) {
                    float ratio = (float) videoNativeHeight / videoNativeWidth;
                    int videoLayoutHeight = (int) (ratio * screenWidth);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(screenWidth, videoLayoutHeight);
                    mediaVideo.setmWidth(screenWidth);
                    mediaVideo.setmHeight(videoLayoutHeight);

                    mediaVideo.setLayoutParams(layoutParams);
                    mediaVideo.setVisibility(View.VISIBLE);
                    mediaVideo.setAvailableCallBack(new SuccessfullCallback() {
                        @Override
                        public void success(Object... params) {
                            mediaVideo.playVideo();
                        }
                    });
                    mediaVideo.loadVideo(media.getVideos().getStandardResolution().getUrl(), media);
                }
            } else {
                mediaVideo.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.imvBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.imvRepost)
    public void repostMedia() {
        if (media.getVideos() != null && media.getVideos().getStandardResolution() != null
                && StringUtils.isNotEmpty(media.getVideos().getStandardResolution().getUrl())) {
            Utils.currentActivity = RepostActivity.this;
            Intent intent = new Intent(RepostActivity.this, DownloadProgress.class);
            intent.putExtra("path", media.getVideos().getStandardResolution().getUrl());
            intent.putExtra("repost", "yes");
            intent.putExtra("caption", media.getCaption().getText());
            intent.putExtra("username", media.getUser().getUsername());
            RepostActivity.this.startActivity(intent);
            Utils.showToast(RepostActivity.this, RepostActivity.this.getString(R.string.preparing_repost));
            return;
        }
        RepostActivity.this.saveImage(true, true);
    }

    @OnClick(R.id.imvShare)
    public void shareMedia() {
        if (media.getVideos() != null && media.getVideos().getStandardResolution() != null
                && StringUtils.isNotEmpty(media.getVideos().getStandardResolution().getUrl())) {
            Utils.currentActivity = RepostActivity.this;
            Intent intent = new Intent(RepostActivity.this, DownloadProgress.class);
            intent.putExtra("path", media.getVideos().getStandardResolution().getUrl());
            intent.putExtra("repost", "yess");
            intent.putExtra("caption", media.getCaption().getText());
            RepostActivity.this.startActivity(intent);
            return;
        }
        RepostActivity.this.saveImage(true, false);
    }

    @OnClick(R.id.imvSave)
    public void saveMedia(){
        if (media.getVideos() != null && media.getVideos().getStandardResolution() != null
                && StringUtils.isNotEmpty(media.getVideos().getStandardResolution().getUrl())) {
            Utils.currentActivity = RepostActivity.this;
            Intent intent = new Intent(RepostActivity.this, DownloadProgress.class);
            intent.putExtra("path", media.getVideos().getStandardResolution().getUrl());
            intent.putExtra("caption", media.getCaption().getText());
            RepostActivity.this.startActivity(intent);
            Utils.showToast(RepostActivity.this, RepostActivity.this.getString(R.string.download_started_background));
            return;
        }
        RepostActivity.this.saveImage(false, false);
    }

    public void setImageSaved(boolean imageSaved, String path, boolean mSpyMode, boolean mRepost) {
        this.mSaveSate = imageSaved;
        this.mSavedPath = path;
        if (mRepost && this.mSaveSate) {
            Utils.copyToClipBoard(this, getString(R.string.caption), getCaption());
            startShareInstagremIntent();
        } else if (mSpyMode && this.mSaveSate) {
            startShareIntent();
        }
    }

    private void startShareInstagremIntent() {
        if (Utils.checkInstagramInstalled(this)) {
            Intent sharingIntent = new Intent("android.intent.action.SEND");
            sharingIntent.setType("image/*");
            sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(this.mSavedPath)));
            sharingIntent.putExtra("android.intent.extra.TEXT", getCaption());
            sharingIntent.setPackage("com.instagram.android");
            startActivity(sharingIntent);
            return;
        }
        Utils.showToast(this, getString(R.string.error_install_instagram));
    }

    private void startShareIntent() {
        Intent sharingIntent = new Intent("android.intent.action.SEND");
        sharingIntent.addFlags(524288);
        sharingIntent.setType("image/jpeg");
        sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(this.mSavedPath)));
        sharingIntent.putExtra("android.intent.extra.TEXT", this.media.getCaption().getText());
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_title)));
    }

    private void saveImage(boolean spyMode, boolean repost) {
        if (this.mBitmapResult != null) {
            this.mediaImage.buildDrawingCache();
            new SampleSaver(this, this.mBitmapResult, new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date()) + "_EZRepost", Boolean.valueOf(spyMode), Boolean.valueOf(repost))
                    .execute(new Integer[]{Integer.valueOf(0)});
            return;
        }
        Utils.showToast((Activity) this, getString(R.string.wait_media));
    }

    private String getCaption() {
        return "#EzRepost @" + media.getUser().getUsername() + " " + "with" + " @repostigapp\n\n" + media.getCaption().getText();
    }


    public void ShowMessageSaved(String msg) {
        Utils.showToast(this, msg);
    }

    Target mBackgroundTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            if (bitmap == null || bitmap.isRecycled())
                return;

            mBitmap = bitmap;
            mBitmapResult = bitmap;
            mediaImage.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            recycle();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

        /**
         * Recycle bitmap to free memory
         */
        private void recycle() {
            if (mBitmap != null && !mBitmap.isRecycled()) {
                mBitmap.recycle();
                mBitmap = null;
                System.gc();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mBackgroundTarget != null) {
                mBackgroundTarget.onBitmapFailed(null);
                Picasso.with(this).cancelRequest(mBackgroundTarget);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!(this.mBitmapResult == null || this.mBitmapResult == this.mBitmap)) {
            this.mBitmapResult.recycle();
            this.mBitmapResult = null;
        }
    }
}
