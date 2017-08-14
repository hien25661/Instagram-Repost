package londatiga.android.instagram;

import android.os.AsyncTask;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import londatiga.android.instagram.util.Debug;
import londatiga.android.instagram.util.StringUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class InstagramRequest {
    private String mAccessToken;

    public interface InstagramRequestListener {
        void onError(String str);

        void onSuccess(String str);
    }

    private class RequestTask extends AsyncTask<URL, Integer, Long> {
        String endpoint;
        InstagramRequestListener listener;
        String method;
        List<NameValuePair> params;
        String response = "";

        public RequestTask(String method, String endpoint, List<NameValuePair> params, InstagramRequestListener listener) {
            this.method = method;
            this.endpoint = endpoint;
            this.params = params;
            this.listener = listener;
        }

        protected void onCancelled() {
        }

        protected void onPreExecute() {
        }

        protected Long doInBackground(URL... urls) {
            try {
                if (this.method.equals(HttpRequest.METHOD_POST)) {
                    this.response = InstagramRequest.this.requestPost(this.endpoint, this.params);
                } else {
                    this.response = InstagramRequest.this.requestGet(this.endpoint, this.params);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Long.valueOf(0);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            if (this.response.equals("")) {
                this.listener.onError("Failed to process api request");
            } else {
                this.listener.onSuccess(this.response);
            }
        }
    }

    public InstagramRequest() {
        this.mAccessToken = "";
    }

    public InstagramRequest(String accessToken) {
        this.mAccessToken = accessToken;
    }

    public String createRequest(String method, String endpoint, List<NameValuePair> params) throws Exception {
        if (method.equals(HttpRequest.METHOD_POST)) {
            return requestPost(endpoint, params);
        }
        return requestGet(endpoint, params);
    }

    public void createRequest(String method, String endpoint, List<NameValuePair> params, InstagramRequestListener listener) {
        new RequestTask(method, endpoint, params, listener).execute(new URL[0]);
    }

    private String requestGet(String endpoint, List<NameValuePair> params) throws Exception {
        StringBuilder append = new StringBuilder().append("https://i.instagram.com/api/v1/");
        if (endpoint.indexOf("/") != 0) {
            endpoint = "/" + endpoint;
        }
        return get(append.append(endpoint).toString(), params);
    }

    private String requestPost(String endpoint, List<NameValuePair> params) throws Exception {
        StringBuilder append = new StringBuilder().append("https://i.instagram.com/api/v1/");
        if (endpoint.indexOf("/") != 0) {
            endpoint = "/" + endpoint;
        }
        return post(append.append(endpoint).toString(), params);
    }

    public String get(String requestUri, List<NameValuePair> params) throws Exception {
        Exception e;
        Throwable th;
        InputStream stream = null;
        String response = "";
        String requestUrl = requestUri;
        try {
            if (!this.mAccessToken.equals("")) {
                if (params == null) {
                    List<NameValuePair> params2 = new ArrayList(1);
                    try {
                        params2.add(new BasicNameValuePair("access_token", this.mAccessToken));
                        params = params2;
                    } catch (Exception e2) {
                        e = e2;
                        params = params2;
                        try {
                            throw e;
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        params = params2;
                        if (stream != null) {
                            stream.close();
                        }
                        try {
                            throw th;
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }
                params.add(new BasicNameValuePair("access_token", this.mAccessToken));
            }
            if (params != null) {
                String str;
                StringBuilder requestParamSb = new StringBuilder();
                int size = params.size();
                int i = 0;
                while (i < size) {
                    BasicNameValuePair param = (BasicNameValuePair) params.get(i);
                    requestParamSb.append(param.getName() + "=" + param.getValue() + (i != size + -1 ? "&" : ""));
                    i++;
                }
                String requestParam = requestParamSb.toString();
                StringBuilder append = new StringBuilder().append(requestUri);
                if (requestUri.contains("?")) {
                    str = "&" + requestParam;
                } else {
                    str = "?" + requestParam;
                }
                requestUrl = append.append(str).toString();
            }
            Debug.m491i("GET " + requestUrl);
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(requestUrl);
            httpGet.addHeader("Accept-Encoding", "gzip");
            httpGet.addHeader("User-Agent", "Instagram 10.10.0 Android (23/6.0.1; 560dpi; 1440x2560; samsung; SM-N920C; noblelte; samsungexynos7420; en_US)");
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity == null) {
                throw new Exception("Request returns empty result");
            }
            stream = httpEntity.getContent();
            Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                stream = new GZIPInputStream(stream);
            }
            response = StringUtil.streamToString(stream);
            Log.d("Response", response);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                throw new Exception(httpResponse.getStatusLine().getReasonPhrase());
            }
            if (stream != null) {
                stream.close();
            }
            return response;
        } catch (Exception e3) {
            e = e3;
        }
        return "";
    }

    public String post(String requestUrl, List<NameValuePair> params) throws Exception {
        Exception e;
        String response = "";
        try {
            if (!this.mAccessToken.equals("")) {
                if (params == null) {
                    List<NameValuePair> params2 = new ArrayList(1);
                    try {
                        params2.add(new BasicNameValuePair("access_token", this.mAccessToken));
                        params = params2;
                    } catch (Exception e2) {
                        e = e2;
                        params = params2;
                        throw e;
                    }
                }
                params.add(new BasicNameValuePair("access_token", this.mAccessToken));
            }
            Debug.m491i("POST " + requestUrl);
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(requestUrl);
            httpPost.addHeader("Accept-Encoding", "gzip");
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity == null) {
                throw new Exception("Request returns empty result");
            }
            InputStream stream = httpEntity.getContent();
            Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                stream = new GZIPInputStream(stream);
            }
            response = StringUtil.streamToString(stream);
            Debug.m491i("Response " + response);
            if (httpResponse.getStatusLine().getStatusCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                return response;
            }
            throw new Exception(httpResponse.getStatusLine().getReasonPhrase());
        } catch (Exception e3) {
            e = e3;
            throw e;
        }
    }
}
