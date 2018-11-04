package com.rage.bootleggersota.Service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.rage.bootleggersota.R;
import com.rage.bootleggersota.Utils.CheckUpdate;
import com.rage.bootleggersota.Utils.CreateNotification;

public class JobIntentCheckUpdate extends JobService {

    private boolean jobCancelled = false;
    private final String TAG = "BackgroundUpdateCheck";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        Log.d(TAG, "Started");
        CheckUpdate checkUpdate = new CheckUpdate();
        if (checkUpdate.isAbleToCheckUpdate()) {
            if (checkUpdate.isUpdateAvailable()) {
                Log.d(TAG, "Update Available!");
                CreateNotification createNotification = new CreateNotification(getApplicationContext(), getApplicationContext().getString(R.string.notification_description));
                createNotification.buildNotification();
                createNotification.showNotification();
            }
        }

        jobFinished(params, false);
        Log.d(TAG, "Done");
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
}
