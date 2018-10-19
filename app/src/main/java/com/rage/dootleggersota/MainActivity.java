package com.rage.dootleggersota;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.rage.dootleggersota.Fragments.BaseFragment;
import com.rage.dootleggersota.Fragments.UpdateFragment;

public class MainActivity extends AppCompatActivity {

    private int container = R.id.frameLayoutContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UpdateFragment fragment = new UpdateFragment();
        getSupportFragmentManager().beginTransaction().replace(container, fragment).commit();
    }
}
