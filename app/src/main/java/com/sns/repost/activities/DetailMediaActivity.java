package com.sns.repost.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sns.repost.BaseActivity;
import com.sns.repost.R;
import com.sns.repost.adapters.MediaAdapter;
import com.sns.repost.dialog.DialogPreview;
import com.sns.repost.helpers.PlaceHolderDrawableHelper;
import com.sns.repost.helpers.callback.SuccessfullCallback;
import com.sns.repost.helpers.customview.CircleTransform;
import com.sns.repost.helpers.customview.VideoPlayer;
import com.sns.repost.models.Media;
import com.sns.repost.models.User;
import com.sns.repost.services.response.LoadLikedResponse;
import com.sns.repost.utils.Consts;
import com.sns.repost.utils.StringUtils;
import com.sns.repost.utils.Utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nguyenvanhien on 8/10/17.
 */

public class DetailMediaActivity extends BaseActivity {
    @Bind(R.id.media_image)
    ImageView mediaImage;
    @Bind(R.id.media_video)
    VideoPlayer mediaVideo;
    @Bind(R.id.tvCaption)
    TextView tvCaption;
    @Bind(R.id.tvNumberLike)
    TextView tvNumberLike;
    @Bind(R.id.rlt_media)
    FrameLayout rltMedia;

    @Bind(R.id.liner_caption)
    View viewCaption;
    @Bind(R.id.imvAvatar)
    ImageView imvAvatar;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.tvTime)
    TextView tvTime;

    @Bind(R.id.viewHeader)
    View viewHeader;
    @Bind(R.id.viewBottom)
    View viewBottom;
    @Bind(R.id.viewSpace)
    View viewSpace;
    @Bind(R.id.imvBrowseInstagram)
    ImageView imvBrowseInstagram;

    private Media media;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_media);
        ButterKnife.bind(this);
        media = Utils.getMediaBundle(getIntent().getStringExtra(Consts.PARAM_MEDIA));
        initView();
        loadData();
    }

    private void initView() {
        viewSpace.setVisibility(View.INVISIBLE);
    }

    private void loadData() {
        if (media == null) return;
        if (media.getImages() != null && StringUtils.isNotEmpty(media.getImages().getStandardResolution().getUrl())) {
            Glide.with(this).load(media.getImages().getStandardResolution().getUrl())
                    .asBitmap().sizeMultiplier(0.7f)
                    .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(0))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .into(mediaImage);
            float ratioImage = (float) (media.getImages().getStandardResolution().getHeight()) / media.getImages().getStandardResolution().getWidth();
        }
        String captionText = "";
        if (media.getCaption() != null && StringUtils.isNotEmpty(media.getCaption().getText())) {
            captionText = media.getCaption().getText();
        }
        tvCaption.setText(getSpanTextCaption(media, captionText));
        tvCaption.setMovementMethod(LinkMovementMethod.getInstance());
        tvCaption.setGravity(Gravity.TOP | Gravity.LEFT);


        if (media.getLikes() != null && media.getLikes().getCount() != null) {
            tvNumberLike.setText(String.format(Locale.US, "%s likes",
                    NumberFormat.getInstance().format(media.getLikes().getCount())));
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
        Glide.with(this).load(media.getUser().getProfilePicture())
                .transform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .fitCenter()
                .into((imvAvatar));

        tvUserName.setText(media.getUser().getUsername());
        tvTime.setText(Utils.getFormatTime(Long.parseLong(media.getCreated_time()) * 1000));


        imvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(DetailMediaActivity.this, UserPageActivity.class);
                t.putExtra(Consts.USER_ID, media.getUser().getId());
                startActivity(t);
            }
        });

        imvBrowseInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPhotoInInstagram(DetailMediaActivity.this, media.getLink());
            }
        });
    }

    private SpannableString getSpanTextCaption(Media currentMedia, String text) {
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.BLUE);
        ForegroundColorSpan fcsTag = new ForegroundColorSpan(Color.BLACK);
        SpannableString ss = new SpannableString(text);


        if (StringUtils.isNotEmpty(text)) {


            ArrayList<String> hashTag = new ArrayList<>();
            if (currentMedia.getTags() != null && currentMedia.getTags().size() > 0) {
                for (String strUserName : currentMedia.getTags()) {
                    hashTag.add("#" + strUserName);
                }
            }

            Pattern pattern = Pattern.compile("#([^#^\\s^\\n\\r^@]*)+");
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                if (StringUtils.isNotEmpty(matcher.group())) {
                    ClickTagSpan clickTagSpan = new ClickTagSpan(matcher.group(), false);
                    int from = matcher.start();
                    int last = matcher.end();
                    ss.setSpan(clickTagSpan, from, last - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(fcsTag, from, last, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(Color.parseColor("#1674ba")), from, last, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

            }

            Pattern patternUserName = Pattern.compile("@([^#^\\s^\\n\\r^@]*)+");
            Matcher matcherUserName = patternUserName.matcher(text);
            while (matcherUserName.find()) {
                if (StringUtils.isNotEmpty(matcherUserName.group())) {
                    User user = checkUserExist(currentMedia.getUsersinphoto(), matcherUserName.group());
                    if (user != null) {
                        ClickTagSpan clickTagSpan = new ClickTagSpan(matcherUserName.group(), true, user);
                        int from = matcherUserName.start();
                        int last = matcherUserName.end();
                        ss.setSpan(clickTagSpan, from, last, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#1674ba")), from, last, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

            }

        }


        //ss.setSpan(fcs, 0, userName.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return ss;
    }

    class ClickTagSpan extends ClickableSpan {// extend ClickableSpan

        String tag;
        boolean isUserName = false;
        User user;

        public ClickTagSpan(String str, boolean isUser) {
            super();
            this.tag = str;
            this.isUserName = isUser;
        }

        public ClickTagSpan(String str, boolean isUser, User user) {
            super();
            this.tag = str;
            this.isUserName = isUser;
            this.user = user;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            if (this.isUserName) {
                if (user != null) {
                    openUserFeedScreen(user);
                }
            } else {
                openTagScreen(tag);
            }
        }
    }

    private void openTagScreen(String tagName) {
        Intent t = new Intent(this, TagFeedActivity.class);
        t.putExtra(Consts.PARAM_TAG_NAME, tagName.replaceAll("#", ""));
        this.startActivity(t);
    }

    private void openUserFeedScreen(User user) {
        Intent t = new Intent(this, UserPageActivity.class);
        t.putExtra(Consts.USER_ID, user.getId());
        this.startActivity(t);
    }

    private User checkUserExist(ArrayList<LoadLikedResponse.UsersInPhoto> usersInPhotos, String userName) {
        User user = null;
        if (usersInPhotos == null || usersInPhotos.size() == 0) return user;
        for (LoadLikedResponse.UsersInPhoto usersInPhoto : usersInPhotos) {
            if (usersInPhoto.getUser() != null) {
                String k = "@" + usersInPhoto.getUser().getUsername();
                if (userName.equals(k)) {
                    user = usersInPhoto.getUser();
                }
            }
        }
        return user;
    }

    @OnClick(R.id.imvBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.imvBrowseInstagram)
    public void linkInstagram() {
        Utils.showPhotoInInstagram(this,media.getLink());
    }
    @OnClick(R.id.btnRepost)
    public void repost() {
        Utils.openRepostScreen(this, media);
    }
}
