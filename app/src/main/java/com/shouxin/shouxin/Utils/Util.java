package com.shouxin.shouxin.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Util {

    public static class NetworkUtil {
        // 判断是否有网络连接
        public static boolean isNetworkConnected(Context context) {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }
            return false;
        }

        // 判断WIFI是否可用
        public static boolean isWifiConnected(Context context) {
            if (context != null) {
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (info != null) {
                    return info.isAvailable();
                }
            }
            return false;
        }

        // 判断网络连接类型
        public static int getConnectType(Context context) {
            if (context != null) {
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    return info.getType();
                }
            }
            return -1;
        }
    }

    public static void showMessage(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
