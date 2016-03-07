package com.favit.runtime.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

/**
 * Created by hyochan on 2016. 3. 7..
 */
public class RuntimeUtil {
    public static final int PERMISSION_STORAGE = 0;
    public static final int PERMISSION_CAMERA = 1;
    public static final int PERMISSION_ALBUM = 2;

    private static final String TAG = RuntimeUtil.class.getSimpleName();

    public static void checkPermission(final Activity activity, final String permission, final int REQUEST_CODE, final OnPermssionCallBackListener listener) {
        checkPermission(activity, null, permission, REQUEST_CODE, null, listener);
    }

    public static void checkPermission(final Activity activity, final String permission, final int REQUEST_CODE, String msg, final OnPermssionCallBackListener listener) {
        checkPermission(activity, null, permission, REQUEST_CODE, msg, listener);
    }

    public static void checkPermission(final Activity activity, View view, final String permission, final int REQUEST_CODE, String msg, final OnPermssionCallBackListener listener) {
        if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                if (view != null) {
                    if (msg == null) {
                        msg = "해당 기능을 사용하기 위해서는 권한 허용을 해야 합니다.";
                    }
                    Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_CODE);
                                }
                            })
                            .show();
                } else {
                    if (msg != null) {
                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                    }
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_CODE);
                }
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_CODE);
            }

        } else {
            if (listener != null) {
                listener.OnGrantPermission();
            }
        }
    }

    public static boolean verifyPermissions(final Activity activity, int[] grantResults) {
        return verifyPermissions(activity, null, grantResults);
    }

    public static boolean verifyPermissions(final Activity activity, View view, int[] grantResults) {
        if (activity == null) return false;

        if (verifyPermissions(grantResults)) {
            return true;
        } else {
            Toast.makeText(activity, "해당 권한을 거부하여 실행 할 수 없습니다.", Toast.LENGTH_SHORT).show();

            if (view != null) {
                Snackbar.make(view, "'설정-앱-" + activity.getString(activity.getApplicationInfo().labelRes) + "-권한’에서 해당 권한을 허용 할 수 있습니다.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + activity.getApplication().getPackageName()));
                                    activity.startActivity(intent);

                                } catch (ActivityNotFoundException e) {
                                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                    activity.startActivity(intent);

                                }
                            }
                        })
                        .show();
            }
            return false;
        }
    }

    public static boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
