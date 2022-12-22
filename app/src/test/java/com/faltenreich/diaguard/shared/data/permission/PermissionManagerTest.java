package com.faltenreich.diaguard.shared.data.permission;


import org.junit.Assert;
import org.junit.Test;

public class PermissionManagerTest {

    private final PermissionManager permissionManager = PermissionManager.getInstance();

    @Test
    public void hasImplicitPermissionForWriteExternalStorageIfAndroid10OrAbove() {
        Assert.assertFalse(permissionManager.hasImplicitPermission(28, Permission.WRITE_EXTERNAL_STORAGE));
        Assert.assertTrue(permissionManager.hasImplicitPermission(29, Permission.WRITE_EXTERNAL_STORAGE));
        Assert.assertTrue(permissionManager.hasImplicitPermission(30, Permission.WRITE_EXTERNAL_STORAGE));
    }

    @Test
    public void hasImplicitPermissionForPostNotificationsIfAndroid12OrLower() {
        Assert.assertTrue(permissionManager.hasImplicitPermission(32, Permission.POST_NOTIFICATIONS));
        Assert.assertFalse(permissionManager.hasImplicitPermission(33, Permission.POST_NOTIFICATIONS));
        Assert.assertFalse(permissionManager.hasImplicitPermission(34, Permission.POST_NOTIFICATIONS));
    }
}