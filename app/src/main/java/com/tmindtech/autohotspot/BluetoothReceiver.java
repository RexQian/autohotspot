package com.tmindtech.autohotspot;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

public class BluetoothReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    MyOreoWifiManager mMyOreoWifiManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, BluetoothProfile.STATE_DISCONNECTED);
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (!isAutoHotspotEnable(context)) {
                return;
            }
            if (!isDeviceMatch(context, device)) {
                return;
            }
            hotspotOreo(context, state == BluetoothProfile.STATE_CONNECTED);
        }
    }

    private boolean isAutoHotspotEnable(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("isAutoHotspot", false);
    }

    private String getDeviceName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString("deviceName", "");
    }

    private boolean isDeviceMatch(Context context, BluetoothDevice device) {
        if (device == null) {
            return false;
        }
        String configName = getDeviceName(context);
        if (configName.isEmpty()) {
            return true;
        }

        if (device.getName().toLowerCase().equals(configName)) {
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void hotspotOreo(Context context, boolean turnOn) {
        Toast.makeText(context, turnOn ? "开启热点" : "关闭热点", Toast.LENGTH_LONG).show();

        if (mMyOreoWifiManager == null) {
            mMyOreoWifiManager = new MyOreoWifiManager(context);
        }

        if (turnOn) {
            MyOnStartTetheringCallback callback = new MyOnStartTetheringCallback() {
                @Override
                public void onTetheringStarted() {
                }

                @Override
                public void onTetheringFailed() {
                }
            };
            mMyOreoWifiManager.startTethering(callback);
        } else {
            mMyOreoWifiManager.stopTethering();
        }
    }
}
