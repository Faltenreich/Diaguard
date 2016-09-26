package com.faltenreich.diaguard.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.faltenreich.diaguard.DiaguardApplication;

/**
 * Created by Faltenreich on 26.09.2016.
 */

public class NetworkingUtils {

    public static boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) DiaguardApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
