package com.sns.repost.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sns.repost.R;
import com.sns.repost.adapters.MediaAdapter;
import com.sns.repost.helpers.callback.SuccessfullCallback;
import com.sns.repost.helpers.customview.VideoPlayer;
import com.sns.repost.models.Media;
import com.sns.repost.utils.StringUtils;
import com.sns.repost.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by nguyenvanhien on 8/10/17.
 */

public class DialogPreview extends Dialog {
    @Bind(R.id.media_image)
    ImageView mediaImage;
    @Bind(R.id.media_video)
    VideoPlayer mediaVideo;
    private Media media;
    private Context mContext;

    public void setMedia(Media media) {
        this.media = media;
    }

    public DialogPreview(@NonNull Context context) {
        super(context, R.style.full_screen_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_preview);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        ButterKnife.bind(this);
        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(mediaImage);
        pAttacher.update();
        if (media.getImages() != null && StringUtils.isNotEmpty(media.getImages().getStandardResolution().getUrl())) {
            Glide.with(mContext).load(media.getImages().getStandardResolution().getUrl())
                    .sizeMultiplier(0.7f)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .fitCenter()
                    .into(mediaImage);
        }

        if (StringUtils.isNotEmpty(media.getType())) {
            if (media.getVideos()!=null && media.getVideos().getStandardResolution()!=null
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
}
