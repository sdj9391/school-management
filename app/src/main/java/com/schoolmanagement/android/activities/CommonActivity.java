package com.schoolmanagement.android.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.schoolmanagement.android.R;


public class CommonActivity extends BaseActivity {

    /**
     * Key for telling {@link CommonActivity} as to which fragment it should open.
     */
    public static final String INTENT_EXTRAS_FRAGMENT = "intent.extas.create_fragment";
    private Fragment fragment = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        initToolbar(true);
        showFragment();
    }

    private void showFragment() {
        Bundle extras = getIntent().getExtras();
        String fragmentName;
        if (extras != null) {
            fragmentName = extras.getString(INTENT_EXTRAS_FRAGMENT);
           /*if (AnnouncementDetailsFragment.class.getSimpleName().equalsIgnoreCase(fragmentName)) {
                fragment = new AnnouncementDetailsFragment();
            } else if (AssignmentsFragment.class.getSimpleName().equalsIgnoreCase(fragmentName)) {
                fragment = new AssignmentsFragment();
            } else if (AssignmentDetailsFragment.class.getSimpleName().equalsIgnoreCase(fragmentName)) {
                fragment = new AssignmentDetailsFragment();
            } else if (ActivitiesFragment.class.getSimpleName().equalsIgnoreCase(fragmentName)) {
                fragment = new ActivitiesFragment();
            } else if (ActivityDetailsFragment.class.getSimpleName().equalsIgnoreCase(fragmentName)) {
                fragment = new ActivityDetailsFragment();
            } else if (MessagesFragment.class.getSimpleName().equalsIgnoreCase(fragmentName)) {
                fragment = new MessagesFragment();
            } else if (MessageDetailsFragment.class.getSimpleName().equalsIgnoreCase(fragmentName)) {
                fragment = new MessageDetailsFragment();
            } else if (AttendanceFragment.class.getSimpleName().equalsIgnoreCase(fragmentName)) {
                fragment = new AttendanceFragment();
            } else if (PayfeesFragment.class.getSimpleName().equalsIgnoreCase(fragmentName)) {
                fragment = new PayfeesFragment();
            } else if (ResourcesFragment.class.getSimpleName().equalsIgnoreCase(fragmentName)) {
                fragment = new ResourcesFragment();
            } else if (AboutSchoolFragment.class.getSimpleName().equalsIgnoreCase(fragmentName)) {
                fragment = new AboutSchoolFragment();
            } else if (ResourceListFragment.class.getSimpleName().equalsIgnoreCase(fragmentName)) {
                fragment = new ResourceListFragment();
            } else if (ResourceDetailsFragment.class.getSimpleName().equalsIgnoreCase(fragmentName)) {
                fragment = new ResourceDetailsFragment();
            } else {
                DebugLog.e("fragment " + fragmentName + " not found");
            }*/
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView, fragment, null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (!isBackPressHandled()) {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isBackPressHandled()) {
                    return super.onOptionsItemSelected(item);
                } else {
                    finish();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * To check back press is handle in fragment or not
     *
     * @return: true if back press handled
     */
    private boolean isBackPressHandled() {
        /*OnBackPressListener onBackPressListener;
        if (fragment instanceof DashboardFragment) {
            onBackPressListener = ((DashboardFragment) fragment).getOnBackPressListener();
        } else {
            return false;
        }

        if (onBackPressListener == null) {
            return false;
        }
        onBackPressListener.onBackPress();
        return true;*/

        return false;
    }

}
