package com.mrhabibi.activityfactory.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mrhabibi.activityfactory.ActivityFactory;
import com.mrhabibi.activityfactory.BasicActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button_launch_new_activity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityFactory.builder(HostActivity.class, MainActivity.this, new RecursiveFragment()).start();
            }
        });
    }
}
