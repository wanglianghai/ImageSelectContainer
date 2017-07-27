package com.bignerdranch.android.wlhimageselectcontainer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/10/010.
 */

public class MPermissionUtils {
    public static void requestPermissionsResult(Activity activity, int requestCode, String[] permissions) {
        requestPermission(activity, requestCode, permissions);
    }

    /**
     * 请求权限处理
     *
     * @param activity      activity
     * @param requestCode 请求码
     * @param permissions 需要请求的权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static void requestPermission(Activity activity, int requestCode, String[] permissions) {
        //被拒绝的权限
        List<String> deniedPermissions = getDeniedPermissions(activity, permissions);
        if (deniedPermissions.size() > 0) {
            //请求被拒绝的权限
            activity.requestPermissions(deniedPermissions
                            .toArray(new String[deniedPermissions.size()]), requestCode);
        }
    }

    /**
     * 获取权限列表中所有需要授权的权限
     *
     * @param context     上下文
     * @param permissions 权限列表
     * @return
     */
    private static List<String> getDeniedPermissions(Context context, String... permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission: permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }

        return deniedPermissions;
    }
}
