package com.sns.repost.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.provider.Settings.Secure;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sns.repost.R;

import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import londatiga.android.instagram.Instagram;
import londatiga.android.instagram.InstagramSession;
import londatiga.android.instagram.InstagramUser;

public class Util {
    private static final String CLIENT_ID = "1901e51014ad44e3a84653fe64b613cf";
    private static final String CLIENT_SECRET = "2d4401fb4e7d4d119b932df10fa55efe";
    private static String DATA_PATH = "";
    public static final String DIR_APP_NAME = "repost_for_ig";
    public static final String PREFS_NAME = "com.appsfull.repostForInstagram";
    private static final String REDIRECT_URI = "http://www.pinssible.com/oauth";
    static final String TAG = "Util";
    public static String accessToken;
    public static Activity currentActivity = null;
    public static String followCount = "";
    public static String followerCount = "";
    public static String[] imageColumns = new String[]{"_data"};
    private static Instagram mInstagram;
    private static InstagramSession mInstagramSession;
    public static String mediaCount = "";
    public static String userAgent = "Instagram 10.10.0 Android (23/6.0.1; 560dpi; 1440x2560; samsung; SM-N920C; noblelte; samsungexynos7420; en_US)";
    public static InstagramUser userToShowinProfile;
    public static String[] videoColumns = new String[]{"_data"};

    static class C03184 implements OnScanCompletedListener {
        C03184() {
        }

        public void onScanCompleted(String path, Uri uri) {
            Log.v("grokkingandroid", "file " + path + " was scanned seccessfully: " + uri);
        }
    }

    private static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList());

        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                if (!displayedImages.contains(imageUri)) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static int dpToPx(int dp) {
        return (int) (((float) dp) * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (((float) px) / Resources.getSystem().getDisplayMetrics().density);
    }

    public static StoryInfo copyStory(StoryInfo si, StoryInfo mSi) {
        mSi.photo = si.photo;
        mSi.photoHQ = si.photoHQ;
        mSi.likesCount = si.likesCount;
        mSi.username = si.username;
        mSi.caption = si.caption;
        mSi.createdTime = si.createdTime;
        mSi.userPhoto = si.userPhoto;
        mSi.id = si.id;
        mSi.link = si.link;
        mSi.video = si.video;
        mSi.videoHQ = si.videoHQ;
        mSi.userId = si.userId;
        mSi.isStory = si.isStory;
        mSi.isVideo = si.isVideo;
        mSi.isSmall = si.isSmall;
        return mSi;
    }

    public static boolean isPhone(Activity act) {
        return (act.getResources().getConfiguration().screenLayout & 15) != 4;
    }

    public static InstagramSession getInstagramSession(Activity act) {
        if (mInstagram == null) {
            mInstagram = new Instagram(act, act, "1901e51014ad44e3a84653fe64b613cf", CLIENT_SECRET, "http://www.pinssible.com/oauth");
        }
        if (mInstagramSession == null) {
            mInstagramSession = mInstagram.getSession();
        }
        return mInstagramSession;
    }

    public static Instagram getInstagram(Activity act, boolean getNew) {
        if (mInstagram == null || getNew) {
            mInstagram = new Instagram(act, act, "1901e51014ad44e3a84653fe64b613cf", CLIENT_SECRET, "http://www.pinssible.com/oauth");
        }
        return mInstagram;
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < ((long) unit)) {
            return "" + bytes;
        }
        int exp = (int) (Math.log((double) bytes) / Math.log((double) unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        double f = ((double) bytes) / Math.pow((double) unit, (double) exp);
        String s = String.format("%.1f", new Object[]{Double.valueOf(f)});
        if (s.indexOf(".") >= 0) {
            s = s.replaceAll("0*$", "").replaceAll("\\.$", "");
        }
        return s + pre;
    }

    public static String getStringCount(String count) {
        if (count != null) {
            try {
                if (!count.equals("")) {
                    return humanReadableByteCount((long) Integer.parseInt(count), true);
                }
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }

    public static void resetSession() {
        mInstagramSession = null;
        mInstagram = null;
    }

    public static Uri getUri() {
        if (Environment.getExternalStorageState().equalsIgnoreCase("mounted")) {
            return Media.EXTERNAL_CONTENT_URI;
        }
        return Media.INTERNAL_CONTENT_URI;
    }

    public static boolean isKitKat() {
        if (VERSION.SDK_INT >= 19) {
            return true;
        }
        return false;
    }

    public static void copyToClipBoard(final Activity act, final String libelle, final String textToCopy) {
        act.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (VERSION.SDK_INT < 11) {
                        ((ClipboardManager) act.getSystemService("clipboard")).setText(textToCopy);
                    } else {
                        try {
                            ((android.content.ClipboardManager) act.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(libelle, textToCopy));
                        } catch (NullPointerException e) {
                        } catch (SecurityException e2) {
                        } catch (RuntimeException e3) {
                        }
                    }
                    Util.showToast(act, act.getString(R.string.copy_caption));
                } catch (IllegalStateException e4) {
                    Log.e("EZRepost", "IllegalStateException raised when accessing clipboard.");
                }
            }
        });
    }

    public static void unbindDrawables(View view) {
        if (view != null) {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ImageView) {
                ((ImageView) view).setImageBitmap(null);
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    unbindDrawables(viewGroup.getChildAt(i));
                }
                if (!(view instanceof AdapterView)) {
                    viewGroup.removeAllViews();
                }
            }
        }
    }

    public static String getTimestamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_EZRepost";
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

    public static String getAppDataTmpPath() {
        String path = getAppDataPath();
        File samplesDir = new File(path + "tmp" + File.separator);
        if (!samplesDir.exists()) {
            samplesDir.mkdirs();
        }
        return path + "tmp" + File.separator;
    }

    public static void showToast(final Activity act, final String msg) {
        act.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(act, msg, 0).show();
            }
        });
    }

    public static void showToastCenter(final Activity act, final String msg) {
        act.runOnUiThread(new Runnable() {
            public void run() {
                Toast toast = Toast.makeText(act, msg, 0);
                toast.setGravity(17, 0, 0);
                toast.show();
            }
        });
    }

    public static void showToast(Context act, String msg) {
        Toast.makeText(act, msg, 0).show();
    }

    public static boolean checkFacbookInstalled(Context act) {
        return checkAppInstalled(act, "com.facebook.katana");
    }

    public static boolean checkTwitterInstalled(Context act) {
        return checkAppInstalled(act, "com.twitter.android");
    }

    public static boolean checkInstagramInstalled(Context act) {
        return checkAppInstalled(act, "com.instagram.android");
    }

    public static boolean checkAppInstalled(Context context, String packagename) {
        try {
            context.getPackageManager().getPackageInfo(packagename, 1);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static String getLikeText(String likes) {
        try {
            if (Integer.parseInt(likes) > 1) {
                return likes + " likes";
            }
            return likes + " like";
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            return "0 like";
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
            sharingIntent.setType(type);
            sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(mSavedPath)));
            sharingIntent.putExtra("android.intent.extra.TEXT", caption);
            sharingIntent.setPackage("com.instagram.android");
            act.startActivity(sharingIntent);
        }
    }

    public static void startShareIntent(Activity act, String mSavedPath, String caption) {
        Intent sharingIntent = new Intent("android.intent.action.SEND");
        sharingIntent.setType("video/*");
        sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(mSavedPath)));
        sharingIntent.putExtra("android.intent.extra.TEXT", caption);
        act.startActivity(Intent.createChooser(sharingIntent, act.getString(R.string.share_title)));
    }

    public static void writeToFile(String filename, String content) {
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


    public static boolean isIntentAvailable(Context ctx, Intent intent) {
        List<ResolveInfo> list = ctx.getPackageManager().queryIntentActivities(intent, 65536);
        if (list == null || list.size() <= 0) {
            return false;
        }
        return true;
    }

    public static void showProfileInInstagram(Context act, String username) {
        try {
            act.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("instagram://user?username=" + username)));
        } catch (ActivityNotFoundException e) {
            act.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://instagram.com/" + username)));
        }
    }

    public static Spannable removeUnderlines(Spannable p_Text) {
        for (URLSpan span : (URLSpan[]) p_Text.getSpans(0, p_Text.length(), URLSpan.class)) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            p_Text.setSpan(new URLSpanNoUnderline(span.getURL()), start, end, 0);
        }
        return p_Text;
    }

    public static double getScreenSizeInches(Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double screenInches = Math.sqrt(Math.pow((double) (((float) dm.widthPixels) / dm.xdpi), 2.0d) + Math.pow((double) (((float) dm.heightPixels) / dm.ydpi), 2.0d));
        Log.d("debug", "Screen inches : " + screenInches);
        return screenInches;
    }

    public static final String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance(CommonUtils.MD5_INSTANCE);
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte b : messageDigest) {
                String h = Integer.toHexString(b & 255);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.toString());
            return "";
        }
    }

    public static String getDeviceId(Activity act) {
        String android_id = Secure.getString(act.getContentResolver(), "android_id");
        Log.i("device id=", md5(android_id).toUpperCase());
        return md5(android_id).toUpperCase();
    }

    public static void showPhotoInInstagram(Context act, String s) {
        if (checkInstagramInstalled(act)) {
            Intent ss = new Intent("android.intent.action.VIEW", Uri.parse(s));
            ss.setPackage("com.instagram.android");
            if (ss.resolveActivity(act.getPackageManager()) != null) {
                act.startActivity(ss);
                return;
            } else {
                showToast(act, act.getString(R.string.error));
                return;
            }
        }
        showToast(act, act.getString(R.string.error_install_instagram));
    }


    public static void scanFile(Activity act, String path) {
        MediaScannerConnection.scanFile(act.getApplicationContext(), new String[]{path}, null, new C03184());
    }

    public static int getScreenWidth(Context c) {
        if (null != null) {
            return 0;
        }
        Display display = ((WindowManager) c.getSystemService("window")).getDefaultDisplay();
        Point size = new Point();
        return display.getWidth();
    }
}
