package me.piebridge.prevent.framework.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.piebridge.prevent.framework.PreventLog;
import me.piebridge.prevent.ui.PreventProvider;

/**
 * Created by thom on 15/8/11.
 */
public class LogcatUtils {

    private LogcatUtils() {

    }

    public static void logcat(Context context) {
        try {
            PreventLog.d("will execute logcat -d -v theradtime");
            Process process = Runtime.getRuntime().exec("/system/bin/logcat -d -v threadtime");
            BufferedInputStream stdout = new BufferedInputStream(process.getInputStream());

            int length;
            byte[] buffer = new byte[0x400];
            ContentResolver contentResolver = context.getContentResolver();
            String path = new SimpleDateFormat("yyyyMMdd.HH.mm.ss'.log'", Locale.US).format(new Date());
            while ((length = stdout.read(buffer)) != -1) {
                String line = new String(buffer, 0, length);
                Uri uri = PreventProvider.CONTENT_URI.buildUpon().appendQueryParameter("path", path)
                        .appendQueryParameter("log", line).build();
                contentResolver.query(uri, null, null, null, null);
            }
            stdout.close();
        } catch (IOException e) {
            PreventLog.d("exec wrong", e);
        }
    }

}