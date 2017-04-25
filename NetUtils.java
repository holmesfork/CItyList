package com.example.holmesk.citylist;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 作者：holmes k
 * 时间：2017.04.25 15:52
 */


public class NetUtils {


    private HttpURLConnection connection;

    public String getCitys(String path) {

        try {
            URL url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                InputStream inputStream = connection.getInputStream();
                byte b[] = new byte[1024];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int len = 0;
                while ((len = inputStream.read(b)) != -1) {
                    bos.write(b, 0, len);
                }
                return bos.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }
}
