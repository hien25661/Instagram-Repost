package londatiga.android.instagram.util;

import android.util.Log;

public class Debug {
    public static void m492i(String tag, String message) {
        Log.i(tag, message);
    }

    public static void m491i(String message) {
        m492i(Cons.TAG, message);
    }

    public static void m489e(String tag, String message) {
        Log.e(tag, message);
    }

    public static void m488e(String message) {
        m489e(Cons.TAG, message);
    }

    public static void m490e(String tag, String message, Exception e) {
        Log.e(tag, message);
        e.printStackTrace();
    }
}
