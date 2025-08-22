package com.robokidz.controller.wifihelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.robokidz.controller.ControlActivity;

public class WifiStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action != null) {
            if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

                if (networkInfo != null && networkInfo.isConnected()) {
                    // Wi-Fi is connected
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String ssid = wifiInfo.getSSID();
                    ControlActivity.setStatus("on");

                    Toast.makeText(context, "Connected to Wi-Fi SSID: " + ssid, Toast.LENGTH_SHORT).show();
                } else {
                    // Wi-Fi is disconnected
//                    Toast.makeText(context, "Wi-Fi is disconnected", Toast.LENGTH_SHORT).show();
                    ControlActivity.setStatus("off");

                }
            }
        }
    }
}
