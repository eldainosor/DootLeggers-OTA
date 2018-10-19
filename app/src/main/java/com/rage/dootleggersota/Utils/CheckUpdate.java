package com.rage.dootleggersota.Utils;

import android.util.Log;

import java.util.ArrayList;

public class CheckUpdate {

    private static final String TAG = "CheckUpdate";
    private ArrayList<String> data = new ArrayList<>();

    private void setup () {
        try {
            CheckUpdateTask task = new CheckUpdateTask();
            task.execute("");
            data = task.get();
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    public boolean isDeviceOfficial () {
        setup();
        return data.size() > 0;
    }

}
