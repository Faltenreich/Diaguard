package com.faltenreich.diaguard.util.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionManager {

    private static PermissionManager instance;

    public static PermissionManager getInstance() {
        if (instance == null) {
            instance = new PermissionManager();
        }
        return instance;
    }

    public boolean hasPermission(Activity activity, Permission permission) {
        return ContextCompat.checkSelfPermission(activity, permission.code) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(Activity activity, Permission permission, PermissionUseCase useCase) {
        requestPermission(activity, new Permission[]{permission}, useCase);
    }

    public void requestPermission(Activity activity, Permission[] permissions, PermissionUseCase useCase) {
        String[] permissionStrings = new String[permissions.length];
        for (int index = 0; index < permissions.length; index++) {
            permissionStrings[index] = permissions[index].code;
        }
        ActivityCompat.requestPermissions(activity, permissionStrings, useCase.requestCode);
    }
}
