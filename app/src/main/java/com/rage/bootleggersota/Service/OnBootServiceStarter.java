package com.rage.bootleggersota.Service;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class OnBootServiceStarter extends BroadcastReceiver {

    private static final String TAG = "Activity";


    @Override
    public void onReceive(Context context, Intent intent) {
        //start service here
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            ComponentName componentName = new ComponentName(context, JobIntentCheckUpdate.class);
            JobInfo info = new JobInfo.Builder(123, componentName)
                    .setRequiresCharging(false)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .setPeriodic(15 * 60 * 60 * 1000)
                    .build();

            JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
            int resultCode = scheduler.schedule(info);
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.d(TAG, "Job scheduled");
            } else {
                Log.d(TAG, "Job scheduling failed");
            }
        }

    }

}
