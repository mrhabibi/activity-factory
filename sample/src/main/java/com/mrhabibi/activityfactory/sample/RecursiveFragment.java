package com.mrhabibi.activityfactory.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mrhabibi.activityfactory.ActivityFactory;

/**
 * Created by mrhabibi on 5/23/17.
 */
public class RecursiveFragment extends Fragment {

    private static final int REQUEST_CODE = 123;
    public static final String RESULT_DATA = "result";

    TextView infoText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_recursive, container, false);

        Button launchButton = (Button) fragmentView.findViewById(R.id.button_launch_new_activity);
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityFactory.builder(HostActivity.class, getContext(), new RecursiveFragment()).startForResult(REQUEST_CODE);
            }
        });

        Button firstButton = (Button) fragmentView.findViewById(R.id.button_first);
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra(RESULT_DATA, "first button");
                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();
            }
        });

        Button secondButton = (Button) fragmentView.findViewById(R.id.button_second);
        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra(RESULT_DATA, "second button");
                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();
            }
        });

        Button thirdButton = (Button) fragmentView.findViewById(R.id.button_third);
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra(RESULT_DATA, "third button");
                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();
            }
        });

        infoText = (TextView) fragmentView.findViewById(R.id.textview_info);
        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            infoText.setText("You chose " + data.getStringExtra(RESULT_DATA));
            infoText.setVisibility(View.VISIBLE);
        }
    }
}
