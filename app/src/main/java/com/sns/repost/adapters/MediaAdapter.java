package com.sns.repost.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.sns.repost.R;
import com.sns.repost.activities.DetailMediaActivity;
import com.sns.repost.activities.TagFeedActivity;
import com.sns.repost.activities.UserPageActivity;
import com.sns.repost.dialog.DialogPreview;
import com.sns.repost.helpers.EventBusUtils;
import com.sns.repost.helpers.PlaceHolderDrawableHelper;
import com.sns.repost.helpers.callback.SuccessfullCallback;
import com.sns.repost.helpers.customview.CircleTransform;
import com.sns.repost.helpers.customview.VideoPlayer;
import com.sns.repost.loader.UserLoader;
import com.sns.repost.models.Media;
import com.sns.repost.models.User;
import com.sns.repost.models.UsersInPhoto;
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

/**
 * Created by Hien on 5/4/2017.
 */

public class MediaAdapter extends UltimateViewAdapter {
    Context mContext;
    ArrayList<Media> mediaList = new ArrayList<>();
    UserLoader userLoader = null;
    private boolean isShowActionHidden = true;
    private boolean isFiltering = false;
    private boolean isUserSelf = false;
    private boolean isUserFeed = false;

    private User userData;
    // variable to track event time
    private long mLastClickTime = 0;
    private static final int MAX_CHAR = 200;
    private int numColumn = 3;
    private Activity mAct;

    public boolean isUserSelf() {
        return isUserSelf;
    }

    public void setUserSelf(boolean userSelf) {
        isUserSelf = userSelf;
    }

    public boolean isUserFeed() {
        return isUserFeed;
    }

    public void setUserFeed(boolean userFeed) {
        isUserFeed = userFeed;
    }

    public int getNumColumn() {
        return numColumn;
    }

    public void setNumColumn(int numColumn) {
        this.numColumn = numColumn;
    }

    public MediaAdapter(ArrayList<Media> list) {
        this.mediaList = list;
    }

    public User getUserData() {
        return userData;
    }

    public void setUserData(User userData) {
        this.userData = userData;
    }

    public boolean isFiltering() {
        return isFiltering;
    }

    public void setFiltering(boolean filtering) {
        isFiltering = filtering;
    }

    public void setShowActionHidden(boolean showActionHidden) {
        isShowActionHidden = showActionHidden;
    }
    public ArrayList<Media> getMediaList() {
        return mediaList;
    }

    public void setMediaList(ArrayList<Media> mediaList) {
        this.mediaList = mediaList;
    }

    public void setmAct(Activity mAct) {
        this.mAct = mAct;
    }

    @Override
    public RecyclerView.ViewHolder newFooterHolder(View view) {
        return new UltimateRecyclerviewViewHolder<>(view);
    }

    @Override
    public RecyclerView.ViewHolder newHeaderHolder(View view) {
        return new UltimateRecyclerviewViewHolder<>(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_set_media_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        mContext = v.getContext();
        userLoader = UserLoader.getInstance();
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return mediaList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (position >= 0 && position < mediaList.size()) {
            final Media media = mediaList.get(position);
            int maxCaptionHeight = 0;

            if(isUserFeed){
                ((ViewHolder) holder).viewHeader.setVisibility(View.GONE);
                ((ViewHolder) holder).viewBottom.setVisibility(View.GONE);
                ((ViewHolder) holder).viewCaption.setVisibility(View.GONE);
                ((ViewHolder) holder).viewSpace.setVisibility(View.GONE);
            }

            if (media.getImages() != null && StringUtils.isNotEmpty(media.getImages().getStandardResolution().getUrl())) {
                Glide.with(mContext).load(media.getImages().getStandardResolution().getUrl())
                        .asBitmap().sizeMultiplier(0.7f)
                        .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .fitCenter()
                        .into(((ViewHolder) holder).mediaImage);
                float ratioImage = (float) (media.getImages().getStandardResolution().getHeight()) / media.getImages().getStandardResolution().getWidth();
                maxCaptionHeight = (int) (ratioImage * Utils.getScreenWidth());
                Log.e("HIEN25661", "" + maxCaptionHeight);

            }
            String captionText = "";
            if (media.getCaption() != null && StringUtils.isNotEmpty(media.getCaption().getText())) {
                captionText = media.getCaption().getText();
            }
            ((ViewHolder) holder).tvCaption.setText(getSpanTextCaption(media, captionText));
            ((ViewHolder) holder).tvCaption.setMovementMethod(LinkMovementMethod.getInstance());
            ((ViewHolder) holder).tvCaption.setGravity(Gravity.TOP | Gravity.LEFT);


            if (media.getLikes() != null && media.getLikes().getCount() != null) {
                ((ViewHolder) holder).tvNumberLike.setText(String.format(Locale.US, "%s likes",
                        NumberFormat.getInstance().format(media.getLikes().getCount())));
            }
            if (StringUtils.isNotEmpty(media.getType())) {
                if (media.getVideos() != null && media.getVideos().getStandardResolution() != null
                        && StringUtils.isNotEmpty(media.getVideos().getStandardResolution().getUrl())) {
                    int videoNativeWidth = media.getVideos().getStandardResolution().getWidth();
                    int videoNativeHeight = media.getVideos().getStandardResolution().getHeight();
                    int screenWidth = Utils.getScreenWidth();
                    if(isUserFeed){
                        screenWidth = (int) (Utils.getScreenWidth()/(getNumColumn()));
                    }
                    if (videoNativeWidth != 0 && videoNativeHeight != 0) {
                        float ratio = (float) videoNativeHeight / videoNativeWidth;
                        int videoLayoutHeight = (int) (ratio * screenWidth);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(screenWidth, videoLayoutHeight);
                        ((ViewHolder) holder).mediaVideo.setmWidth(screenWidth);
                        ((ViewHolder) holder).mediaVideo.setmHeight(videoLayoutHeight);

                        ((ViewHolder) holder).mediaVideo.setLayoutParams(layoutParams);
                        ((ViewHolder) holder).mediaVideo.setVisibility(View.VISIBLE);
                        ((ViewHolder) holder).mediaVideo.setAvailableCallBack(new SuccessfullCallback() {
                            @Override
                            public void success(Object... params) {
                                ((ViewHolder) holder).mediaVideo.playVideo();
                            }
                        });
                        ((ViewHolder) holder).mediaVideo.loadVideo(media.getVideos().getStandardResolution().getUrl(), media);
                    }
                } else {
                    ((ViewHolder) holder).mediaVideo.setVisibility(View.GONE);
                }
            }
            Glide.with(mContext).load(media.getUser().getProfilePicture())
                    .transform(new CircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .fitCenter()
                    .into(((ViewHolder) holder).imvAvatar);

            ((ViewHolder) holder).tvUserName.setText(media.getUser().getUsername());
            ((ViewHolder) holder).tvTime.setText(Utils.getFormatTime(Long.parseLong(media.getCreated_time()) * 1000));


            ((ViewHolder) holder).rltMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isUserFeed){
                        openDetailMedia(media);
                    }else {
                        DialogPreview dialogPreview = new DialogPreview(mContext);
                        dialogPreview.setMedia(media);
                        dialogPreview.show();
                    }
                }
            });
            ((ViewHolder) holder).imvAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent t = new Intent(mContext, UserPageActivity.class);
                    t.putExtra(Consts.USER_ID,media.getUser().getId());
                    mContext.startActivity(t);
                }
            });

            ((ViewHolder) holder).imvBrowseInstagram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.showPhotoInInstagram(mAct,media.getLink());
                }
            });

            ((ViewHolder) holder).btnRepost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.openRepostScreen(mContext,media);
                }
            });
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class ViewHolder extends UltimateRecyclerviewViewHolder {
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
        @Bind(R.id.btnRepost)
        TextView btnRepost;

        @Bind(R.id.itemview)
        View item_view;

        @Bind(R.id.imvBrowseInstagram)
        ImageView imvBrowseInstagram;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public Media getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position >= 0 && position < mediaList.size())
            return mediaList.get(position);
        else return null;
    }

    public void insert(Media media, int position) {
        insertInternal(mediaList, media, position);
    }

    public void remove(int position) {
        removeInternal(mediaList, position);
    }

    public void clear() {
        clearInternal(mediaList);
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
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
    private void openTagScreen(String tagName){
        Intent t = new Intent(mContext,TagFeedActivity.class);
        t.putExtra(Consts.PARAM_TAG_NAME,tagName.replaceAll("#",""));
        mContext.startActivity(t);
    }
    private void openUserFeedScreen(User user){
        Intent t = new Intent(mContext,UserPageActivity.class);
        t.putExtra(Consts.USER_ID,user.getId());
        mContext.startActivity(t);
    }

    private void openDetailMedia(Media media){
        Intent t = new Intent(mContext,DetailMediaActivity.class);
        t.putExtra(Consts.PARAM_MEDIA,media.toJson());
        mContext.startActivity(t);
    }


    private User checkUserExist(ArrayList<UsersInPhoto> usersInPhotos, String userName) {
        User user = null;
        if (usersInPhotos == null || usersInPhotos.size() == 0) return user;
        for (UsersInPhoto usersInPhoto : usersInPhotos) {
            if (usersInPhoto.getUser() != null) {
                String k = "@" + usersInPhoto.getUser().getUsername();
                if (userName.equals(k)) {
                    user = usersInPhoto.getUser();
                }
            }
        }
        return user;
    }

}
