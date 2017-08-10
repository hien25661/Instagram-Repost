package com.sns.repost.api;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Debug;

import com.sns.repost.R;
import com.sns.repost.activities.RepostActivity;
import com.sns.repost.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;

public class SampleSaver extends AsyncTask<Integer, Integer, Boolean> {
    boolean imageSaved = false;
    Activity mActivity;
    Bitmap mBitmap;
    String mName;
    boolean mRepost;
    boolean mSpyMode;

    public SampleSaver(Activity activity, Bitmap bitmap, String name, Boolean spyMode, Boolean repost) {
        this.mName = name;
        this.mActivity = activity;
        this.mSpyMode = spyMode.booleanValue();
        this.mRepost = repost.booleanValue();
        this.mBitmap = bitmap;
    }

    protected Boolean doInBackground(Integer... params) {
        File samplesDir = new File(Utils.getAppDataPath());
        if (samplesDir.exists()) {
        } else {
            samplesDir.mkdirs();
        }
        try {
            if (this.mBitmap == null || this.mBitmap.isRecycled()) {
                this.imageSaved = false;
            } else {
                this.mBitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(Utils.getAppDataPath() + this.mName + ".jpg"));
                MediaScannerConnection.scanFile(this.mActivity.getApplicationContext(), new String[]{Utils.getAppDataPath() + this.mName + ".jpg"}, null, null);
                this.imageSaved = true;
            }
            ((RepostActivity) this.mActivity).setImageSaved(this.imageSaved, Utils.getAppDataPath() + this.mName + ".jpg", this.mSpyMode, this.mRepost);
        } catch (Exception e) {
            e.printStackTrace();
            this.imageSaved = false;
            ((RepostActivity) this.mActivity).setImageSaved(this.imageSaved, Utils.getAppDataPath() + this.mName + ".jpg", this.mSpyMode, this.mRepost);
        } catch (Throwable th) {
            Throwable th2 = th;
            ((RepostActivity) this.mActivity).setImageSaved(this.imageSaved, Utils.getAppDataPath() + this.mName + ".jpg", this.mSpyMode, this.mRepost);
        }
        return null;
    }

    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (!this.mSpyMode && this.imageSaved) {
            ((RepostActivity) this.mActivity).ShowMessageSaved(this.mActivity.getString(R.string.media_saved)
                    + " " + Utils.getAppDataPath() + this.mName + ".jpg");
        }
    }
}
