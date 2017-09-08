package com.wzes.huddle.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.baidu.mapapi.UIMsg.m_AppUI;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapTools {
    public static Bitmap getImage(String address) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(address).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(m_AppUI.MSG_APP_GPS);
        byte[] imagebytes = getBytes(conn.getInputStream());
        return BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);
    }

    public static byte[] getBytes(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = is.read(buffer);
            if (len != -1) {
                bos.write(buffer, 0, len);
            } else {
                is.close();
                bos.flush();
                byte[] result = bos.toByteArray();
                System.out.println(new String(result));
                return result;
            }
        }
    }
}
