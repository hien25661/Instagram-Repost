package londatiga.android.instagram;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sns.repost.R;
import com.sns.repost.helpers.customview.InstagramWebView;
import com.sns.repost.utils.LoggedInUser;

@SuppressLint({"NewApi", "SetJavaScriptEnabled"})
public class InstagramDialog extends Dialog {
    static final LayoutParams FILL = new LayoutParams(-1, -1);
    static final int MARGIN = 8;
    static final int PADDING = 2;
    static final String TAG = "Instagram-Android";
    private Activity mActivity;
    private String mAuthUrl;
    private LinearLayout mContent;
    private Context mContext;
    private InstagramDialogListener mListener;
    private String mRedirectUri;
    private ProgressDialog mSpinner;
    private TextView mTitle;
    private WebView mWebView;

    public interface InstagramDialogListener {
        void onCancel();

        void onError(String str);

        void onSuccess(LoggedInUser loggedInUser);

        void onSuccess(String str);
    }

    private class InstagramWebViewClient extends WebViewClient {
        private InstagramWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(InstagramDialog.TAG, "Redirecting URL " + url);
            if (!url.startsWith(InstagramDialog.this.mRedirectUri)) {
                return false;
            }
            if (url.contains("code")) {
                InstagramDialog.this.mListener.onSuccess(url.split("=")[1]);
            } else if (url.contains("error")) {
                String[] temp = url.split("=");
                InstagramDialog.this.mListener.onError(temp[temp.length - 1]);
            }
            InstagramDialog.this.dismiss();
            return true;
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            InstagramDialog.this.mListener.onError(description);
            InstagramDialog.this.dismiss();
            Log.d(InstagramDialog.TAG, "Page error: " + description);
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            try {
                InstagramDialog.this.mSpinner.show();
            } catch (Exception e) {
            }
            Log.d(InstagramDialog.TAG, "Loading URL: " + url);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String title = InstagramDialog.this.mWebView.getTitle();
            if (title != null && title.length() > 0) {
                InstagramDialog.this.mTitle.setText(title);
            }
            if (InstagramDialog.this.mSpinner != null && InstagramDialog.this.mSpinner.isShowing()) {
                InstagramDialog.this.mSpinner.dismiss();
            }
        }
    }

    public InstagramDialog(Context context, Activity act, String authUrl, String redirectUri, InstagramDialogListener listener) {
        super(context);
        this.mContext = context;
        this.mAuthUrl = authUrl;
        this.mListener = listener;
        this.mRedirectUri = redirectUri;
        this.mActivity = act;
    }

    protected void onCreate(Bundle savedInstanceState) {
        int width;
        int height;
        super.onCreate(savedInstanceState);
        this.mSpinner = new ProgressDialog(getContext());
        this.mSpinner.requestWindowFeature(1);
        this.mSpinner.setMessage("Loading...");
        this.mContent = new LinearLayout(getContext());
        this.mContent.setOrientation(1);
        setUpTitle();
        setUpWebView();
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        Point outSize = new Point();
        double[] dimensions = new double[2];
        if (VERSION.SDK_INT >= 13) {
            display.getSize(outSize);
            width = outSize.x;
            height = outSize.y;
        } else {
            width = display.getWidth();
            height = display.getHeight();
        }
        if (width < height) {
            dimensions[0] = 0.87d * ((double) width);
            dimensions[1] = 0.82d * ((double) height);
        } else {
            dimensions[0] = ((double) width) * 0.75d;
            dimensions[1] = ((double) height) * 0.75d;
        }
        addContentView(this.mContent, new LayoutParams((int) dimensions[0], (int) dimensions[1]));
    }

    private void setUpTitle() {
        requestWindowFeature(1);
        Drawable icon = getContext().getResources().getDrawable(R.drawable.icon);
        this.mTitle = new TextView(getContext());
        this.mTitle.setText("login");
        this.mTitle.setTextColor(-1);
        this.mTitle.setTypeface(Typeface.DEFAULT_BOLD);
        this.mTitle.setBackgroundColor(-15321261);
        this.mTitle.setPadding(10, 8, 8, 8);
        this.mTitle.setCompoundDrawablePadding(10);
        this.mTitle.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        this.mContent.addView(this.mTitle);
    }

    private void setUpWebView() {
        this.mWebView = new WebView(this.mActivity);
        this.mWebView.setVerticalScrollBarEnabled(false);
        this.mWebView.setHorizontalScrollBarEnabled(false);
        this.mWebView.setWebViewClient(new InstagramWebView(this, this.mActivity, this.mListener, this.mSpinner));
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.loadUrl("file:///android_asset/MotionHobby.html");
        this.mWebView.setLayoutParams(FILL);
        WebSettings webSettings = this.mWebView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        this.mContent.addView(this.mWebView);
    }

    public void clearCache() {
        this.mWebView.clearCache(true);
        this.mWebView.clearHistory();
        this.mWebView.clearFormData();
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.mListener.onCancel();
    }
}
