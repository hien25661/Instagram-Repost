package com.sns.repost.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.sns.repost.RepostApplication;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.Request;

public class Constants {
    public static final String API_URL = "https://api.instagram.com/v1";
    public static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    public static final String BASE_URL = "https://i.instagram.com/api/v1/";
    public static final String CALLBACK_URL = "http://www.pinssible.com/oauth";
    public static final String CLIENT_ID = "1901e51014ad44e3a84653fe64b613cf";
    public static final String CLIENT_SECRET = "";
    public static final String CONSTANT_UID = "uuid";
    public static final String HELPER_URL = "https://instagram.com/accounts/login/?next";
    public static final String IMAGE_FILE_DIR = "repostapp/images";
    public static final String SELF = "self";
    public static final String TEST_URL = "https://api.instagram.com/v1/media/popular?access_token=511030605.79c46a7.abf34637b9bf4c44b87974511f11a6bc";
    public static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    public static final String VIDEO_FILE_DIR = "repostapp/vids";
    public static String next_url;

    public static void RapidRepostLog(String paramString1, String paramString2) {
    }

    public static final String encode(String paramString) {
        try {
            return URLEncoder.encode(paramString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return paramString;
        }
    }

    public static String getAuthUrl() {
        return String.format("%s?client_id=%s&redirect_uri=%s&response_type=code&display=touch&scope=likes+comments+relationships", new Object[]{AUTH_URL, CLIENT_ID, CALLBACK_URL});
    }


    public static String getMediaByTag(String paramString) {
        return "https://i.instagram.com/api/v1/feed/tag/" + paramString + "/";
    }

    public static HashMap<String, String> getParms(String paramString) {
        HashMap localHashMap = new HashMap();
        String[] arrayOfString1 = paramString.split("[?]");
        if (arrayOfString1.length > 1) {
            for (String split : arrayOfString1[1].split("[&]")) {
                String[] arrayOfString3 = split.split("[=]");
                if (arrayOfString3.length > 1) {
                    localHashMap.put(arrayOfString3[0], arrayOfString3[1]);
                }
            }
        }
        return localHashMap;
    }

    public static String getPopular() {
        return "https://i.instagram.com/api/v1/feed/popular/?people_teaser_supported=1";
    }

    public static String getRelatedTags(String paramString) {
        return String.format("%stags/search/?q=%s", new Object[]{"https://i.instagram.com/api/v1/", encode(paramString)});
    }

    public static String getSelfFeedUrl() {
        return "https://i.instagram.com/api/v1/feed/timeline/";
    }

    public static String getSelfLikedUrl() {
        return "https://i.instagram.com/api/v1/feed/liked/";
    }

    public static String getUserAboutUrl(String paramString) {
        return "https://i.instagram.com/api/v1/users/" + paramString + "/info/";
    }

    public static String getUserFollowedUrl(String paramString) {
        return "https://i.instagram.com/api/v1/friendships/" + paramString + "/followers/";
    }

    public static String getUserFollowsUrl(String paramString) {
        return "https://i.instagram.com/api/v1/friendships/" + paramString + "/following/";
    }

    public static String getUserLikedUrl(String paramString) {
        return String.format("%s/users/%s/media/liked?access_token=%s", new Object[]{API_URL, paramString,
                RepostApplication.getInstance().getAppSettings().getInstagramAccessToken()});
    }

    public static String getUserMediaUrl(String paramString) {
        return "https://i.instagram.com/api/v1/feed/user/" + paramString + "/";
    }

    public static String getUsersByQuery(String paramString) {
        return String.format("%susers/search/?query=%s", new Object[]{"https://i.instagram.com/api/v1/", encode(paramString)});
    }

    public static boolean isIntentAvailable(Context paramContext, Intent paramIntent) {
        if (paramIntent == null) {
        }
        if (paramContext.getPackageManager().queryIntentActivities(paramIntent, 65536).size() <= 0) {
            return false;
        }
        return true;
    }

    public static String requestEncryptor(String paramString) {
        try {
            SecretKeySpec localSecretKeySpec = new SecretKeySpec("9b3b9e55988c954e51477da115c58ae82dcae7ac01c735b4443a3c5923cb593a".getBytes("UTF-8"), "HmacSHA256");
            Mac localMac = Mac.getInstance("HmacSHA256");
            localMac.init(localSecretKeySpec);
            byte[] arrayOfByte = localMac.doFinal(paramString.getBytes("UTF-8"));
            StringBuffer localStringBuffer = new StringBuffer();
            for (byte b : arrayOfByte) {
                String str2 = Integer.toHexString(b & 255);
                if (str2.length() == 1) {
                    localStringBuffer.append('0');
                }
                localStringBuffer.append(str2);
            }
            return localStringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (InvalidKeyException e2) {
            return "";
        } catch (UnsupportedEncodingException e3) {
            return "";
        }
    }

    public static Request requestreformer(Request paramRequest) {
        return paramRequest.newBuilder().header("User-Agent", "Instagram 8.0.0 Android (18/4.3; 320dpi; 720x1280; Xiaomi; HM 1SW; armani; qcom; en_US)").header("Connection", "close").header("Accept-Language", "en-US").header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8").header("Accept", "*/*").header("Cookie2", "$Version=1").method(paramRequest.method(), paramRequest.body()).removeHeader("Accept-Encoding").build();
    }

    private boolean videoFileExists(String paramString) {
        String str = Environment.getExternalStoragePublicDirectory(VIDEO_FILE_DIR).getAbsolutePath();
        File localFile = new File(str);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        return new File(str + "/" + paramString).exists();
    }

    public boolean imageFileExists(String paramString) {
        String str = Environment.getExternalStoragePublicDirectory(IMAGE_FILE_DIR).getAbsolutePath();
        File localFile = new File(str);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        return new File(str + "/" + paramString).exists();
    }
}
