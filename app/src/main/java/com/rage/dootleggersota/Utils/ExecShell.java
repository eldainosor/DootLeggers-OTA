package com.rage.dootleggersota.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

//Executes a shell command
public class ExecShell {

    public ExecShell () {

    }

    private static final String TAG = "ExecShell";

    public String exec (String command) {
        String output = "";
        Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = "";
            while (true) {
                line = reader.readLine();
                if (line == null)
                    break;
                output = output + "\n" + line;
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        return output;
    }

}
