package com.sns.repost.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sns.repost.BuildConfig;
import com.sns.repost.RepostApplication;
import com.sns.repost.activities.RepostActivity;
import com.sns.repost.adapters.MediaAdapter;
import com.sns.repost.helpers.customview.CircleTransform;
import com.sns.repost.models.Images;
import com.sns.repost.models.Media;
import com.sns.repost.models.StandardResolution;
import com.sns.repost.models.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.sns.repost.R;
import com.sns.repost.models.Videos;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramSession;


/**
 * Created by Hien on 5/6/2017.
 */

public class Utils {
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private static String DATA_PATH = "";
    public static final String DIR_APP_NAME = "repost_for_ig";
    public static Activity currentActivity = null;
    public static String userAgent = "Instagram 8.0.0 Android (18/4.3; 320dpi; 720x1280; Xiaomi; HM 1SW; armani; qcom; en_US)";

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }


    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }


    public static void hideKeyboard(final View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getString(Context context, int id) {
        return context.getString(id);
    }


    public static boolean checkNotLogin() {
        if (RepostApplication.getInstance().getAppSettings().getInstagramAccessToken().equals("")
                || RepostApplication.getInstance().getAppSettings().getInstagramAccessToken().equals(BuildConfig.DEFAULT_TOKEN)) {
            return true;
        }
        return false;
    }

    public static int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";


    public static String getCurrentDateFormat() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static String getDateBeforeOrAfterSomeWeek(int week) {
        /*Date now = new Date();

        Date resultDate;
        resultDate = new Date(now.getTime() + 604800 * week * 1000);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(resultDate);*/
        Calendar cal = Calendar.getInstance();

        if (week > 0) {
            cal.add(Calendar.DATE, -7 * week);
        } else {
            cal.add(Calendar.DATE, 7 * week);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(cal.getTime());
    }

    public static String getDateAddingBefore(int i) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -i);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(cal.getTime());
    }


    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = BuildConfig.APPLICATION_ID;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcess.processName.equals(packageName)) {
                //                Log.e("app",appPackageName);
                return true;
            }
        }
        return false;
    }

    public static boolean checkUserExist(ArrayList<User> list, User user) {
        if (list.size() == 0) return false;
        for (User mUser : list) {
            if (mUser.getId().equals(user.getId())) return true;
        }
        return false;
    }

    public static ArrayList<Media> getFilterMediaUser(ArrayList<Media> list, User user) {
        ArrayList<Media> mediaArrayList = new ArrayList<>();
        for (Media item : list) {
            if (item.getUser().getId().equalsIgnoreCase(user.getId())) {
                mediaArrayList.add(item);
            }
        }

        return mediaArrayList;
    }

    public static void updateUserList(ArrayList<User> list, User user) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(user.getId())) {
                list.set(i, user);
                break;
            }
        }
    }

    public static void setUserForMedia(ArrayList<User> list, Media media) {
        if (list.size() > 0) {
            for (User mUser : list) {
                if (mUser.getId().equals(media.getUser().getId())) {
                    media.setUser(mUser);
                    break;
                }
            }
        }
    }

    public static PointF mapPoint(PointF pointF, float scaleFactorX, float scaleFactorY) {
       /* Matrix mat = new Matrix();
        mat.postScale(scaleFactorX,scaleFactorY);
        float pts[] = {pointF.x, pointF.y};
        mat.mapPoints(pts);*/
        return new PointF(pointF.x * scaleFactorX, pointF.y * scaleFactorY);
    }

    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static int roundNumber(double x) {
        return (int) Math.ceil(x);
    }


    /**
     * Used to scroll to the given view.
     *
     * @param scrollViewParent Parent ScrollView
     * @param view             View to which we need to scroll.
     */
    public static void scrollToView(final NestedScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y);
    }

    /**
     * Used to get deep child offset.
     * <p/>
     * 1. We need to scroll to child in scrollview, but the child may not the direct child to scrollview.
     * 2. So to get correct child position to scroll, we need to iterate through all of its parent views till the main parent.
     *
     * @param mainParent        Main Top parent.
     * @param parent            Parent.
     * @param child             Child.
     * @param accumulatedOffset Accumalated Offset.
     */
    public static void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

    public static String getFormatTime(long timeMillis) {
        long time = Math.max(System.currentTimeMillis() - timeMillis, 0);
        if (time >= DateUtils.YEAR_IN_MILLIS) {
            return (time / DateUtils.YEAR_IN_MILLIS) + " y.";
        }
        if (time >= DateUtils.WEEK_IN_MILLIS) {
            return (time / DateUtils.WEEK_IN_MILLIS) + " w.";
        }
        if (time >= DateUtils.DAY_IN_MILLIS) {
            return (time / DateUtils.DAY_IN_MILLIS) + " d.";
        }
        if (time >= DateUtils.HOUR_IN_MILLIS) {
            return (time / DateUtils.HOUR_IN_MILLIS) + " h.";
        }
        return (time / DateUtils.MINUTE_IN_MILLIS) + " min.";
    }

    public static void showImage(Context mContext, String url, ImageView imv) {
        Glide.with(mContext).load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .fitCenter()
                .into(imv);
    }

    public static boolean checkMediaContain(ArrayList<Media> mList, Media item) {
        if (mList == null || mList.size() == 0) return false;
        for (Media media : mList) {
            if (media.getId().equals(item.getId())) {
                return true;
            }
        }
        return false;
    }


    public static boolean checkAppInstalled(Context context, String packagename) {
        try {
            context.getPackageManager().getPackageInfo(packagename, 1);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean checkInstagramInstalled(Context act) {
        return checkAppInstalled(act, "com.instagram.android");
    }

    public static void showToast(final Activity act, final String msg) {
        act.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(act, msg, 0).show();
            }
        });
    }

    public static void showPhotoInInstagram(Context act, String s) {
        if (checkInstagramInstalled(act)) {
            Intent ss = new Intent("android.intent.action.VIEW", Uri.parse(s));
            ss.setPackage("com.instagram.android");
            if (ss.resolveActivity(act.getPackageManager()) != null) {
                act.startActivity(ss);
                return;
            } else {
                showToast((Activity) act, act.getString(R.string.error));
                return;
            }
        }
        showToast((Activity) act, act.getString(R.string.error_install_instagram));
    }

    public static Media getMediaBundle(String json) {
        Media media = null;
        Gson gson = new Gson();
        media = gson.fromJson(json, Media.class);
        return media;
    }

    public static void openRepostScreen(Context context, Media media) {
        Intent t = new Intent(context, RepostActivity.class);
        t.putExtra(Consts.PARAM_MEDIA, media.toJson());
        context.startActivity(t);
    }

    public static String getAppDataPath() {
        if (DATA_PATH.equals("")) {
            DATA_PATH = Environment.getExternalStorageDirectory() + File.separator + DIR_APP_NAME + File.separator;
        }
        File samplesDir = new File(DATA_PATH);
        if (!samplesDir.exists()) {
            samplesDir.mkdirs();
        }
        return DATA_PATH;
    }

    public static void copyToClipBoard(final Activity act, final String libelle, final String textToCopy) {
        act.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (Build.VERSION.SDK_INT < 11) {
                        ((ClipboardManager) act.getSystemService("clipboard")).setText(textToCopy);
                    } else {
                        try {
                            ((android.content.ClipboardManager) act.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(libelle, textToCopy));
                        } catch (NullPointerException e) {
                        } catch (SecurityException e2) {
                        } catch (RuntimeException e3) {
                        }
                    }
                    Utils.showToast(act, act.getString(R.string.copy_caption));
                } catch (IllegalStateException e4) {
                    Log.e("SNSRepost", "IllegalStateException raised when accessing clipboard.");
                }
            }
        });
    }

    public static String getTimestamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_SNSRepost";
    }

    public static void copyFile(File source, File dest) throws IOException {
        InputStream in = new FileInputStream(source);
        OutputStream out = new FileOutputStream(dest);
        byte[] buf = new byte[1024];
        while (true) {
            int len = in.read(buf);
            if (len > 0) {
                out.write(buf, 0, len);
            } else {
                in.close();
                out.close();
                return;
            }
        }
    }

    public static void startShareInstagremIntent(Activity act, String mSavedPath, String caption) {
        if (checkInstagramInstalled(act)) {
            Intent sharingIntent = new Intent("android.intent.action.SEND");
            String type = "";
            if (mSavedPath.contains("mp4")) {
                type = "video/*";
            } else {
                type = "image/*";
            }
            Uri photoURI = null;
            photoURI = FileProvider.getUriForFile(act,act.
                    getString(R.string.file_provider_authority),
                    new File(mSavedPath));
            sharingIntent.setType(type);
            sharingIntent.putExtra("android.intent.extra.STREAM", photoURI);
            sharingIntent.putExtra("android.intent.extra.TEXT", caption);
            sharingIntent.setPackage("com.instagram.android");
            act.startActivity(sharingIntent);
        }
    }

    public static void startShareIntent(Activity act, String mSavedPath, String caption) {
        Intent sharingIntent = new Intent("android.intent.action.SEND");
        sharingIntent.setType("video/*");
        Uri photoURI = null;
        photoURI = FileProvider.getUriForFile(act,act.
                        getString(R.string.file_provider_authority),
                new File(mSavedPath));
        sharingIntent.putExtra("android.intent.extra.STREAM", photoURI);
        sharingIntent.putExtra("android.intent.extra.TEXT", caption);
        act.startActivity(Intent.createChooser(sharingIntent, act.getString(R.string.share_title)));
    }

    public static void scanFile(Activity act, String path) {
        MediaScannerConnection.scanFile(act.getApplicationContext(), new String[]{path}, null, new scanListener());
    }

    static class scanListener implements MediaScannerConnection.OnScanCompletedListener {
        scanListener() {
        }

        public void onScanCompleted(String path, Uri uri) {
            Log.v("grokkingandroid", "file " + path + " was scanned seccessfully: " + uri);
        }
    }

    private static Instagram mInstagram;
    private static InstagramSession mInstagramSession;


    // You may uncomment next line if using Android Annotations library, otherwise just be sure to run it in on the UI thread
// @UiThread
    public static String getDefaultUserAgentString(Context context) {
        if (Build.VERSION.SDK_INT >= 17) {
            return NewApiWrapper.getDefaultUserAgent(context);
        }

        try {
            Constructor<WebSettings> constructor = WebSettings.class.getDeclaredConstructor(Context.class, WebView.class);
            constructor.setAccessible(true);
            try {
                WebSettings settings = constructor.newInstance(context, null);
                return settings.getUserAgentString();
            } finally {
                constructor.setAccessible(false);
            }
        } catch (Exception e) {
            return new WebView(context).getSettings().getUserAgentString();
        }
    }

    @TargetApi(17)
    static class NewApiWrapper {
        static String getDefaultUserAgent(Context context) {
            return WebSettings.getDefaultUserAgent(context);
        }
    }
    public static InstagramSession getInstagramSession(Activity act) {
        if (mInstagram == null) {
            mInstagram = new Instagram(act,
                    BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET, BuildConfig.CALLBACK_URL);
        }
        if (mInstagramSession == null) {
            mInstagramSession = mInstagram.getSession();
        }
        return mInstagramSession;
    }

    public static Instagram getInstagram(Activity act, boolean getNew) {
        if (mInstagram == null || getNew) {
            mInstagram = new Instagram(act,
                    BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET, BuildConfig.CALLBACK_URL);
        }
        return mInstagram;
    }
    public static void writeToFile(String filename, String content) {
    }

    public static Media getMediaFromStoryInfo(StoryInfo storyInfo){
        Media media = new Media();
        media.setId(storyInfo.id);

        Images images = new Images();
        StandardResolution standardResolution = new StandardResolution();
        standardResolution.setUrl(storyInfo.photoHQ);
        images.setStandardResolution(standardResolution);
        media.setImages(images);

        Videos videos = new Videos();


        return media;
    }
}
