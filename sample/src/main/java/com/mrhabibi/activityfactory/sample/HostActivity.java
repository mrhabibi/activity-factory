package com.mrhabibi.activityfactory.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mrhabibi.activityfactory.BasicActivity;

/**
 * Created by mrhabibi on 5/23/17.
 */

public class HostActivity extends BasicActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
    }
}
