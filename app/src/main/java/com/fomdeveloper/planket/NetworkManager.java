package com.fomdeveloper.planket;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Fernando on 28/09/16.
 */
public class NetworkManager {

    private ConnectivityManager connectivityManager;

    public NetworkManager(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public boolean isConnectedToInternet(){
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnected();
    }
}
