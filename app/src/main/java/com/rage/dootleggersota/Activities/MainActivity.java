package com.rage.dootleggersota.Activities;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.rage.dootleggersota.Fragments.BaseFragment;
import com.rage.dootleggersota.Fragments.UnableFragment;
import com.rage.dootleggersota.Fragments.UpdateFragment;
import com.rage.dootleggersota.R;
import com.rage.dootleggersota.Utils.CheckUpdate;
import com.rage.dootleggersota.Utils.ExecShell;

public class MainActivity extends AppCompatActivity {

    private int container = R.id.frameLayoutContainer;
    private View refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkUpdate();
        defineLayout();
    }

    private void defineLayout () {
        refresh = findViewById(R.id.viewCheckUpdate);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Checking..", Toast.LENGTH_SHORT).show();
                checkUpdate();
            }
        });
    }

    private void checkUpdate () {
        CheckUpdate checkUpdate = new CheckUpdate();
        if (checkUpdate.isAbleToCheckUpdate()) {
            if (checkUpdate.isUpdateAvailable()) {
                UpdateFragment fragment = new UpdateFragment();
                fragment.setArguments(checkUpdate.getData());
                getSupportFragmentManager().beginTransaction().replace(container, fragment).commit();
            }
            else {
                BaseFragment fragment = new BaseFragment();
                getSupportFragmentManager().beginTransaction().replace(container, fragment).commit();
            }
        }
        else {// check for internet connection
            UnableFragment fragment = new UnableFragment();
            getSupportFragmentManager().beginTransaction().replace(container, fragment).commit();
        }
    }
}
