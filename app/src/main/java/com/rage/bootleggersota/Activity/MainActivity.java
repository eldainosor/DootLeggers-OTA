package com.rage.bootleggersota.Activity;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rage.bootleggersota.Fragments.BaseFragment;
import com.rage.bootleggersota.Fragments.UpdateFragment;
import com.rage.bootleggersota.R;
import com.rage.bootleggersota.Service.JobIntentCheckUpdate;
import com.rage.bootleggersota.Utils.CheckUpdate;
import com.rage.bootleggersota.Utils.CreateNotification;
import com.rage.bootleggersota.Utils.ExecShell;

import java.io.File;

public class MainActivity extends FragmentActivity {

    private final int FRAGMENT_CONTAINER_ID = R.id.frameLayoutFragment;
    private FrameLayout fragmentContainer;
    private TextView txBuildCode, txBuildCodename;
    private View refresh;
    private final String TAG = "MainActivity";
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defineLayouts();
        setCustomAcionBar();
        setCodenameAndBuild();
        managePermission();
        // folder making
        File folder = new File(Environment.getExternalStorageDirectory() + getApplicationContext().getResources().getString(R.string.directory));
        folder.mkdirs();
        String basetext = getApplicationContext().getResources().getString(R.string.notif_main_help);
        setBaseFragment(basetext);
        startservice();
    }

    // sets layouts and their listener
    private void defineLayouts() {
        fragmentContainer = findViewById(FRAGMENT_CONTAINER_ID);
        txBuildCode = findViewById(R.id.textViewBuildCode);
        txBuildCodename = findViewById(R.id.textViewCodename);
        toolbar = findViewById(R.id.toolbarMain);
    }

    private void setCustomAcionBar () {
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflates the menu; and adds items to the action bar if present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handles action bar clicks here
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            Toast.makeText(getApplicationContext(), getString(R.string.action_refresh_message), Toast.LENGTH_SHORT).show();
            checkUpdate();
            return true;
        }
        else if (id == R.id.action_settings) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Calls the check update class and manages the update status
    private void checkUpdate() {
        File folder = new File(Environment.getExternalStorageDirectory() + getApplicationContext().getResources().getString(R.string.directory));
        folder.mkdirs(); // just to be safe : )
        CheckUpdate checkUpdate = new CheckUpdate();
        if (checkUpdate.isAbleToCheckUpdate()) {
            if (checkUpdate.isUpdateAvailable()) {
                UpdateFragment updateFragment = new UpdateFragment();
                updateFragment.setArguments(checkUpdate.getData());
                replaceFragmentWithAnimation(updateFragment);
            } else {
                String notext = getApplicationContext().getResources().getString(R.string.notif_no_updates);
                setBaseFragment(notext);
            }
        } else {
            String unabletext = getApplicationContext().getResources().getString(R.string.notif_unable_check);
            setBaseFragment(unabletext);
        }

    }

    // sets base fragment with the message provided
    private void setBaseFragment(String message) {
        BaseFragment fragment = new BaseFragment();
        Bundle data = new Bundle();
        data.putString("info", message);
        fragment.setArguments(data);
        replaceFragmentWithAnimation(fragment);
    }

    // reads and sets codename on the top part
    private void setCodenameAndBuild() {
        ExecShell execShell = new ExecShell();
        String codename = execShell.exec("getprop ro.bootleggers.songcodename");
        codename = codename.trim();
        String build = execShell.exec("getprop ro.bootleggers.version_number");
        build = "v" + build.trim();
        txBuildCodename.setText(codename);
        txBuildCode.setText(build);
    }

    // helper method to replace fragments
    private void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(FRAGMENT_CONTAINER_ID, fragment);
        transaction.commit();
    }


    //////////////////////////////////////////////
    // permission part
    private final int REQUEST_CODE = 123;

    private void managePermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted
                File folder = new File(Environment.getExternalStorageDirectory() + "/BootleggersOTA");
                folder.mkdirs();
            } else {
                Toast.makeText(getApplicationContext(), "Storage permission is required to manage files!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /////////////////////////////////////////////

    // starts the service to check update in the background.
    public void startservice() {
        ComponentName componentName = new ComponentName(getApplicationContext(), JobIntentCheckUpdate.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(1)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }



}
