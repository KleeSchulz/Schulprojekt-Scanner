package com.scanner.fbs_scanner;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;

public class Datei{

    private static final Activity ActivityCompat = 112;

    public static void erstelleVerzeichnis(String DNAME) {


        public static final int REQUEST_WRITE_STORAGE = 112;

       private requestPermission(Activity context) {
            boolean hasPermission = (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            } else {
                // You are allowed to write external storage:
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/new_folder";
                File storageDir = new File(path);
                if (!storageDir.exists() && !storageDir.mkdirs()) {
                    // This should never happen - log handled exception!
                }
            }


        File directory = new File(Environment.getExternalStorageDirectory()+File.separator+DNAME);
        Log.e("Log", directory.toString());

    }
}