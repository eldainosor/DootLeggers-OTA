package com.rage.dootleggersota.Utils;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class CheckUpdate {

    private static final String TAG = "CheckUpdate";
    private static final String PROP_COMMAND_PREFIX = "getprop ";
    private static final String DELIMITER = "-";
    private ArrayList<String> data = new ArrayList<>();
    private ExecShell execShell = new ExecShell();

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

    public boolean isAbleToCheckUpdate () {
        setup();
        return data.size() > 0;
    }

    public boolean isUpdateAvailable () {
        String currentVersion = execShell.exec(PROP_COMMAND_PREFIX+"ro.bootleggers.version");
        String onlineVersion = data.get(1).substring(data.get(1).indexOf('=')+1);
        String currentBuildDate = currentVersion.substring(currentVersion.lastIndexOf(DELIMITER)+1);
        String onlineBuildDate = onlineVersion.substring(currentVersion.lastIndexOf(DELIMITER)+1);
        return !currentBuildDate.equals(onlineBuildDate);
    }

    public Bundle getData () {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("data", data);
        return bundle;
    }

    public void setData (Bundle bundle) {
        data = bundle.getStringArrayList("data");
    }

}
