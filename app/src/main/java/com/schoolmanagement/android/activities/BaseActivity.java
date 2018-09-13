package com.schoolmanagement.android.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.schoolmanagement.R;
import com.schoolmanagement.android.utils.DebugLog;

public class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize the {@link Toolbar} to show back button.
     */
    public void initToolbar(boolean backIndicator) {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        } else {
            DebugLog.w("Toolbar is null");
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(backIndicator);
            actionBar.setHomeButtonEnabled(backIndicator);
        } else {
            DebugLog.w("Actionbar null");
        }
    }

    /**
     * Hides the toolbar, if any.
     */
    public void hideToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        } else {
            DebugLog.w("Toolbar is null");
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        } else {
            DebugLog.w("Actionbar null");
        }
    }

    public void setToolBarTitle(String title) {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) {
            getSupportActionBar().setTitle(title);
            return;
        }

        TextView titleTextView = toolbar.findViewById(R.id.toolbar_title);
        if (titleTextView == null) {
            super.setTitle(title);
        } else {
            super.setTitle("");
            titleTextView.setText(title);
        }
    }

    public void setToolBarSubTitle(String title) {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) {
            getSupportActionBar().setSubtitle(title);
            return;
        }

        TextView subTitleTextView =
                toolbar.findViewById(R.id.toolbar_sub_title);
        if (subTitleTextView == null) {
            getSupportActionBar().setSubtitle(title);
        } else {
            subTitleTextView.setText(title);
            subTitleTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * A wrapper to show any kind of message to the user.
     * Wrapper is used so that the underlying error showing mechanism can be changed later on.
     *
     * @param errorMessage
     */
    public void showMessage(String errorMessage) {

        try {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                    errorMessage, Snackbar.LENGTH_SHORT);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
