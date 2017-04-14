package com.mrhabibi.activityfactory.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by mrhabibi on 11/30/16.
 * Utility class that used to keep and get passed fragment from PersistentDialog builder to created
 * BasicActivity, you know, fragment can't be passed via intent, that's why this class is made.
 * One time usage, it means that it just keeps the fragment just once, and once the fragment
 * fetched, it will be removed from the map, because it may cause leak if it's still kept staticly
 */
public class FragmentPasser {

    private static final HashMap<String, Fragment> passedFragment = new HashMap<>();

    @Nullable
    public static Fragment getFragment(String getterId) {
        if (getterId != null && passedFragment.containsKey(getterId)) {
            Fragment fragment = passedFragment.get(getterId);
            passedFragment.remove(getterId);
            return fragment;
        }
        return null;
    }

    public static String setFragment(@NonNull Fragment fragment) {

        String fragmentGetterId = UUID.randomUUID().toString();
        passedFragment.put(fragmentGetterId, fragment);

        return fragmentGetterId;
    }

}
