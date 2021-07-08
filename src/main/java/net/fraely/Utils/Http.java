package net.fraely.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Http {

    public static String getjson(String mid) {
        URL geturl = null;
        try {
            geturl = new URL(mid);
            HttpURLConnection connection = (HttpURLConnection) geturl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(4 * 1000);
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/70.0.3538.102");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String data = result.toString(StandardCharsets.UTF_8.name());
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
