package com.rage.dootleggersota.Activities;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.rage.dootleggersota.Fragments.BaseFragment;
import com.rage.dootleggersota.Fragments.UpdateFragment;
import com.rage.dootleggersota.R;
import com.rage.dootleggersota.Utils.CheckUpdate;

public class MainActivity extends AppCompatActivity {

    private int container = R.id.frameLayoutContainer;
    private View refresh;
    private FrameLayout frameLayout;
    private Animation fadeIn;
    private Animation fadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defineLayout();
        BaseFragment fragment = new BaseFragment();
        Bundle data = new Bundle();
        data.putString("middle", "Check for updates?");
        data.putBoolean("showCard", true);
        data.putString("bottom", "To check for updates,\nTap the refresh button.");
        fragment.setArguments(data);
        replaceFragmentWithAnimation(fragment);
        //checkUpdate();
    }

    private void defineLayout () {
        refresh = findViewById(R.id.viewCheckUpdate);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdate();
            }
        });
        frameLayout =  findViewById(R.id.frameLayoutContainer);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
    }

    private void checkUpdate () {
        CheckUpdate checkUpdate = new CheckUpdate();
        if (checkInternet()) {
            if (checkUpdate.isAbleToCheckUpdate()) {
                if (checkUpdate.isUpdateAvailable()) {
                    Toast.makeText(getApplicationContext(), "Update Available!", Toast.LENGTH_SHORT).show();
                    UpdateFragment fragment = new UpdateFragment();
                    fragment.setArguments(checkUpdate.getData());
                    replaceFragmentWithAnimation(fragment);
                } else {
                    Toast.makeText(getApplicationContext(), "No Update Available", Toast.LENGTH_SHORT).show();
                    BaseFragment fragment = new BaseFragment();
                    replaceFragmentWithAnimation(fragment);
                }
            }
        }
        else {// check for internet connection
            Bundle data = new Bundle();
            data.putString("middle", "Unable to check for updates");
            data.putBoolean("showCard", true);
            data.putString("bottom", "Check your internet connection and,\nTap the refresh button.");
            BaseFragment fragment = new BaseFragment();
            fragment.setArguments(data);
            replaceFragmentWithAnimation(fragment);
        }
    }

    private void replaceFragmentWithAnimation (Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(container, fragment);
        transaction.commit();
    }

    private boolean checkInternet () {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable() && manager.getActiveNetworkInfo().isConnected();
    }
}
