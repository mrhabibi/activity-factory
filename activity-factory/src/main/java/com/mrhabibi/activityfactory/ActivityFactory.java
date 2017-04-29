package com.mrhabibi.activityfactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import static com.mrhabibi.activityfactory.BasicActivity.FRAGMENT_GETTER_ID_LABEL;

/**
 * Created by mrhabibi on 4/28/17.
 * This is the official ActivityFactory builder class, this is very fit for you whom has application
 * with architecture like "one Activity one Fragment", this will save your cost of making Activities
 * one by one, you just need to implement the fragments only! and one generic activity to hold the
 * fragment inside it, and of course, your manifest file will not be ruined because of declaration
 * of plenty of the same activities, this will improve your productivity very much!
 * If you find any bug, please let me know :)
 */

public class ActivityFactory {

    public final static String TAG = "ActivityFactory";

    /**
     * Builder instantiator with an activity Class as initialization, this is the simplest way if
     * you don't have any extras to be passed inside the intent
     *
     * @param activityClass The activity class and it must inherit BasicActivity class
     * @param context       The God object
     * @param fragment      The fragment inside the activity
     * @return The builder
     */
    public static Builder builder(@NonNull Class<? extends BasicActivity> activityClass, @NonNull Context context, @NonNull Fragment fragment) {
        return new Builder(activityClass, fragment, context);
    }

    /**
     * Builder instantiator with an Intent as initialization, you can pass any extras inside it!
     *
     * @param intent   The intent that contains activity class with extras as you need
     * @param context  The God object
     * @param fragment The fragment inside the activity
     * @return The builder
     */
    public static Builder builder(@NonNull Intent intent, @NonNull Context context, @NonNull Fragment fragment) {
        return new Builder(intent, fragment, context);
    }

    public static class Builder {
        protected Context mContext;
        protected Intent mIntent;
        protected Fragment mFragment;
        protected int mFlags;

        public Builder(@NonNull Intent intent, @NonNull Fragment fragment, @NonNull Context context) {
            try {
                if (!(BasicActivity.class.isAssignableFrom(Class.forName(intent.getComponent().getClassName())))) {
                    throw new IllegalStateException("Intent class for ActivityFactory must inherit BasicActivity class!");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.mIntent = intent;
            this.mContext = context;
            this.mFragment = fragment;
        }

        public Builder(@NonNull Class<? extends BasicActivity> activityClass, @NonNull Fragment fragment, @NonNull Context context) {
            this.mIntent = new Intent(context, activityClass);
            this.mContext = context;
            this.mFragment = fragment;
        }

        /**
         * Setter for custom flags for the intent
         *
         * @param flags The flags
         * @return The builder
         */
        public Builder flags(int flags) {
            this.mFlags = flags;
            return this;
        }

        /**
         * Preparation method before launching the activity, it will always be called before
         * start() or startForResult(), you can override it and add some logic you need
         */
        protected void prepare() {
            Fragment fragment = this.mFragment;
            this.mFragment = null;

            /*
             * Make connection between fragment and activity
             */
            String fragmentGetterId = null;
            if (fragment != null) {
                fragmentGetterId = FragmentPasser.setFragment(fragment);
            }

            mIntent.putExtra(FRAGMENT_GETTER_ID_LABEL, fragmentGetterId);
            mIntent.addFlags(mFlags);
        }

        /**
         * Check and return back the context
         *
         * @return The context
         */
        private Context initContext() {
            Context context = this.mContext;
            this.mContext = null;

            if (context == null) {
                throw new IllegalStateException("Context must not be null!");
            }
            return context;
        }

        /**
         * Start the activity without request code
         */
        public void start() {
            prepare();
            Context context = initContext();
            ContextCompat.startActivity(context, mIntent, null);
        }

        /**
         * Start the activity with request code
         *
         * @param requestCode The activity request code
         */
        public void startForResult(int requestCode) {
            prepare();
            Context context = initContext();
            if (context instanceof Activity) {
                ActivityCompat.startActivityForResult(((Activity) context), mIntent, requestCode, null);
            } else {
                ContextCompat.startActivity(context, mIntent, null);
            }
        }

    }
}
