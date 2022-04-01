package com.example.audiorecorder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permission {
    String[] appPermission = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int permission_Request_Code = 3;

    public boolean checkAndRequest(Context context, Activity activity) {
        List<String> permissionList = new ArrayList<>();
        for (String perm : appPermission)
        {
            if (ContextCompat.checkSelfPermission(context,perm)!= PackageManager.PERMISSION_GRANTED)
            {
                permissionList.add(perm);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]), permission_Request_Code);
            return true;
        }
        return false;
    }
}
