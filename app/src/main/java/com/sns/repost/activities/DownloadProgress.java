package com.sns.repost.activities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.sns.repost.R;
import com.sns.repost.utils.Utils;

import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class DownloadProgress extends Activity {
    String mCaption;
    String mPath;
    String mRepost;
    String mUsername;
    private int progress = 10;
    ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_progress);
        this.mPath = getIntent().getStringExtra("path");
        this.mRepost = getIntent().getStringExtra("repost");
        this.mCaption = getIntent().getStringExtra("caption");
        this.mUsername = getIntent().getStringExtra("username");
        if (this.mPath == null || this.mPath.equals("")) {
            Utils.showToast((Activity) this, getString(R.string.unavailable_private_content));
        } else {
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this, DownloadProgress.class), 0);
            String text = "";
            if (this.mRepost != null && this.mRepost.equals("yes") && this.mRepost.equals("yess")) {
                text = getString(R.string.preparing_repost);
            } else {
                text = getString(R.string.preparing_download);
            }
            final Notification notification = new Notification(R.mipmap.ic_launcher, text, System.currentTimeMillis());
            notification.flags |= 2;
            notification.contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.download_progress);
            notification.contentIntent = pendingIntent;
            notification.contentView.setImageViewResource(R.id.status_icon, R.mipmap.ic_download);
            notification.contentView.setTextViewText(R.id.status_text, text);
            notification.contentView.setProgressBar(R.id.status_progress, 100, this.progress, false);
            Context applicationContext = getApplicationContext();
            getApplicationContext();
            final NotificationManager notificationManager = (NotificationManager) applicationContext.getSystemService("notification");
            notificationManager.notify(42, notification);
            new Thread() {
                public void run() {
                    Looper.prepare();
                    final String fileName = Utils.getAppDataPath() + Utils.getTimestamp() + ".mp4";
                    new AsyncHttpClient().get(DownloadProgress.this.mPath, new FileAsyncHttpResponseHandler(DownloadProgress.this) {
                        public void onSuccess(int statusCode, Header[] headers, File response) {
                            String path = fileName;
                            try {
                                Utils.copyFile(response, new File(fileName));
                                MediaScannerConnection.scanFile(DownloadProgress.this.getApplicationContext(), new String[]{fileName}, null, null);
                                if (Utils.currentActivity != null) {
                                    Utils.showToast(Utils.currentActivity,
                                            Utils.currentActivity.getString(R.string.video_saved) + " " + path);
                                    Utils.scanFile(Utils.currentActivity, path);
                                }
                                if (!(Utils.currentActivity == null || DownloadProgress.this.mRepost == null || !DownloadProgress.this.mRepost.equals("yes"))) {
                                    Utils.copyToClipBoard(DownloadProgress.this,
                                            DownloadProgress.this.getString(R.string.caption), DownloadProgress.this.getCaption());
                                    Utils.startShareInstagremIntent(Utils.currentActivity, path, DownloadProgress.this.getCaption());
                                }
                                if (!(Utils.currentActivity == null || DownloadProgress.this.mRepost == null || !DownloadProgress.this.mRepost.equals("yess"))) {
                                    Utils.startShareIntent(Utils.currentActivity, path, DownloadProgress.this.mCaption);
                                }
                                notificationManager.cancel(42);
                            } catch (IOException e) {
                            }
                        }

                        public void onProgress(long bytesWritten, long totalSize) {
                            DownloadProgress.this.progress = (int) ((((double) bytesWritten) / ((double) totalSize)) * 100.0d);
                            notification.contentView.setProgressBar
                                    (R.id.status_progress, 100, DownloadProgress.this.progress, false);
                            notificationManager.notify(42, notification);
                        }

                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File response) {
                            Utils.showToast(DownloadProgress.this,
                                    Utils.currentActivity.getString(R.string.network_error)
                                    + " " + Utils.currentActivity.getString(R.string.please_try_again));
                        }
                    });
                    Looper.loop();
                }
            }.start();
        }
        finish();
    }

    private String getCaption() {
        return "#SNSRepost @" + this.mUsername + " " + "with" + " @repost\n\n" + this.mCaption;
    }
}
