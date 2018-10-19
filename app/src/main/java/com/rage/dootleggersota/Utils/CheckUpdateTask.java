package com.rage.dootleggersota.Utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class CheckUpdateTask extends AsyncTask <String, Void, ArrayList<String>> {

    private static final String LINK_PREFIX = "https://raw.githubusercontent.com/hdsrivastava/BootlegOTA-Data/master/";
    private static final String PROP_COMMAND_PREFIX = "getprop ";
    private static final String TAG = "CheckUpdateAsyncTask";
    private static ExecShell execShell = new ExecShell();
    private ArrayList<String> data = new ArrayList<>();

    @Override
    protected ArrayList<String> doInBackground(String... string) {
        try {
            String device = execShell.exec(PROP_COMMAND_PREFIX + "ro.bootleggers.device");
            Log.d(TAG, "Device : "+device);
            URL link = new URL(LINK_PREFIX + device);
            BufferedReader reader = new BufferedReader(new InputStreamReader(link.openStream()));
            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                data.add(line);
            }
            reader.close();
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        return data;
    }
}
