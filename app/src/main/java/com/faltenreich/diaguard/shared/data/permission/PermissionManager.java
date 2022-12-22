package com.faltenreich.diaguard.shared.data.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionManager {

    private static PermissionManager instance;

    public static PermissionManager getInstance() {
        if (instance == null) {
            instance = new PermissionManager();
        }
        return instance;
    }

    public boolean hasPermission(Context context, Permission permission) {
        return hasImplicitPermission(Build.VERSION.SDK_INT, permission) || hasExplicitPermission(context, permission);
    }

    public boolean hasImplicitPermission(int sdk, Permission permission) {
        switch (permission) {
            // Permission has been replaced with scoped storage on Android 10+
            case WRITE_EXTERNAL_STORAGE: return sdk >= Build.VERSION_CODES.Q;
            // Permission is required on Android 13+
            case POST_NOTIFICATIONS: return sdk < Build.VERSION_CODES.TIRAMISU;
            default: return false;
        }
    }

    public boolean hasExplicitPermission(Context context, Permission permission) {
        return ContextCompat.checkSelfPermission(context, permission.getCode())
            == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(Activity activity, Permission permission, PermissionUseCase useCase) {
        requestPermission(activity, new Permission[]{permission}, useCase);
    }

    public void requestPermission(Activity activity, Permission[] permissions, PermissionUseCase useCase) {
        String[] permissionStrings = new String[permissions.length];
        for (int index = 0; index < permissions.length; index++) {
            permissionStrings[index] = permissions[index].getCode();
        }
        ActivityCompat.requestPermissions(activity, permissionStrings, useCase.requestCode);
    }
}
