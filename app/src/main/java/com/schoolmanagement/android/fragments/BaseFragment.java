package com.schoolmanagement.android.fragments;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import com.schoolmanagement.android.activities.BaseActivity;
import com.schoolmanagement.android.utils.DebugLog;

public class BaseFragment extends Fragment {

    /**
     * Show back button or not.
     */
    protected void showBackIndicator(boolean backIndicator) {
        if (!isAdded()) {
            DebugLog.e("fragment is not added");
            return;
        }

        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity == null) {
            DebugLog.w(" baseActivity is null");
            return;
        }

        baseActivity.initToolbar(backIndicator);
    }

    /**
     * To set toolbar title to TextView in toolbar layout.
     */
    protected void setToolBarTitle(String string) {
        if (!isAdded()) {
            DebugLog.e("fragment is not added");
            return;
        }

        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity == null) {
            DebugLog.w(" baseActivity is null");
            return;
        }

        baseActivity.setToolBarTitle(string);
    }

    /**
     * To set hide toolbar.
     */
    protected void hideToolbar() {
        if (!isAdded()) {
            DebugLog.e("fragment is not added");
            return;
        }

        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity == null) {
            DebugLog.w(" baseActivity is null");
            return;
        }

        baseActivity.hideToolbar();
    }

    /**
     * To set toolbar subtitle to custom TextView in toolbar layout.
     */
    protected void setToolBarSubTitle(String string) {
        if (!isAdded()) {
            DebugLog.e("fragment is not added");
            return;
        }

        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity == null) {
            DebugLog.w(" baseActivity is null");
            return;
        }

        baseActivity.setToolBarSubTitle(string);
    }

    /**
     * A wrapper to show message to the user.
     * Wrapper is used so that the underlying error showing mechanism can be changed later on.
     *
     * @param message
     */
    protected void showMessage(String message) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showMessage(message);
        } else {
            // this will be executed in the worst condition.
            try {
                if (getView() != null) {
                    Snackbar snackbar = Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    DebugLog.e("Not able to show a message!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
