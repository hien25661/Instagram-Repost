package com.sns.repost.utils;


/**
 * @author tranngocchanthien
 * @since 19/06/2015
 */

public class StringUtils {

    public static final String _TAG_SEARCH = "zAu~";
    private static final String TAG = "StringUtils";
    private static final long ONE_DAY = 24 * 60 * 60 * 1000;

    private static char toHex(int ch) {
        return (char) (ch < 10 ? '0' + ch : 'A' + ch - 10);
    }

    private static boolean isUnsafe(char ch) {
        return ch > 128 || ch < 0 || " %$&+,/:;=?@<>#%".indexOf(ch) >= 0;
    }

    public static String emptyIfNull(String s) {
        return isNotEmpty(s) ? s : "";
    }

    public static boolean isEmpty(String s) {
        return null == s || "".equals(s.trim()) || "null".equals(s) || "none".equals(s);
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static String encodeUrl(String url) {
        StringBuilder resultStr = new StringBuilder();
        for (char ch : url.toCharArray()) {
            if (isUnsafe(ch)) {
                resultStr.append('%');
                resultStr.append(toHex(ch / 16));
                resultStr.append(toHex(ch % 16));
            } else {
                resultStr.append(ch);
            }
        }
        return resultStr.toString();
    }

    public static byte[] hexToBytes(char[] hex) {
        int length = hex.length / 2;
        byte[] raw = new byte[length];
        for (int i = 0; i < length; i++) {
            int high = Character.digit(hex[i * 2], 16);
            int low = Character.digit(hex[i * 2 + 1], 16);
            int value = (high << 4) | low;
            if (value > 127)
                value -= 256;
            raw[i] = (byte) value;
        }
        return raw;
    }

    public static boolean isNumeric(String str) {
        if (str == null || isEmpty(str)) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String markUserSearchText(String text) {
        return String.format("%s%s",_TAG_SEARCH, text.trim());
    }

    public static String clearUserSearchText(String text) {
        if(text.contains(_TAG_SEARCH)) {
            String tmp = text.trim();
            String[] split = tmp.split(_TAG_SEARCH);
            if (split.length > 1) {
                return split[1];
            }
        }
        return text;
    }

}

