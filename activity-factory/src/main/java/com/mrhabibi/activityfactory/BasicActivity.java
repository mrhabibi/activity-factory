package com.mrhabibi.activityfactory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by mrhabibi on 4/28/17.
 * This abstract activity has to be the parent of your activity to be used with ActivityFactory,
 * it just need to implement injectFragmentContainerRes() method to identify where the fragment
 * should be put inside the activity
 */

public abstract class BasicActivity extends AppCompatActivity {

    public final static String FRAGMENT_GETTER_ID_LABEL = "fragmentGetterId";
    public final static String FRAGMENT_TAG = "fragmentTag";

    protected boolean mFirstCreation;
    protected String mFragmentGetterId;
    protected Fragment mCurrentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mFirstCreation = savedInstanceState == null;
        extractBundleStates(getIntent().getExtras());
        super.onCreate(savedInstanceState);
        if (mFirstCreation) {
            mCurrentFragment = getActiveFragment();
        } else {
            mCurrentFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (injectFragmentContainerRes() <= 0) {
            return;
        }

        /*
         * Bring the fragment to live
         */
        if (mFirstCreation) {
            if (findViewById(injectFragmentContainerRes()) == null) {
                throw new IllegalStateException("Fragment container resource id not found, are you sure the id passed in injectFragmentContainerRes() is correct?");
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(injectFragmentContainerRes(), mCurrentFragment, FRAGMENT_TAG)
                    .commit();
            fragmentManager.executePendingTransactions();
        }

        /*
         * Check if the fragment has expired
         */
        if (mFragmentGetterId != null && mCurrentFragment == null) {
            Log.e(ActivityFactory.TAG, "Finishing due to Expired Session");
            finish();
            return;
        }

        onFragmentAttached(mCurrentFragment);
    }

    /**
     * Method that will be called after fragment attached to the container, you can override it
     * and add your own logic
     *
     * @param fragment The attached fragment
     */
    protected void onFragmentAttached(Fragment fragment) {
    }

    /**
     * Get active fragment to be attached to container, you can override it and pass your own
     * fragment
     *
     * @return The fragment
     */
    protected Fragment getActiveFragment() {
        return FragmentPasser.getFragment(mFragmentGetterId);
    }

    /**
     * Resource id of view inside activity to be used as fragment container
     *
     * @return The resource id
     */
    @IdRes
    protected abstract int injectFragmentContainerRes();

    /**
     * Pass the activity result for nested PersistentDialog
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCurrentFragment != null) {
            mCurrentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void extractBundleStates(Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(FRAGMENT_GETTER_ID_LABEL)) {
                mFragmentGetterId = bundle.getString(FRAGMENT_GETTER_ID_LABEL);
            }
        }
    }
}
