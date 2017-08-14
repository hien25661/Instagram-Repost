package londatiga.android.instagram.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtil {
    public static String streamToString(InputStream is) throws IOException {
        String str = "";
        if (is == null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
            reader.close();
            return sb.toString();
        } finally {
            is.close();
        }
    }
}
