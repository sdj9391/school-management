/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.schoolmanagement.android.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.schoolmanagement.android.utils.AppUtils;
import com.schoolmanagement.android.utils.Constant;
import com.schoolmanagement.android.utils.DebugLog;

/**
 * Define a sync adapter for the app.
 * <p>Ø
 * <p>This class is instantiated in {@link SyncService}, which also binds SyncAdapter to the system.
 * SyncAdapter should only be initialized in SyncService, never anywhere else.
 * <p>
 * <p>The system calls onPerformSync() via an RPC call through the IBinder object supplied by
 * SyncService.
 */
class SyncAdapter extends AbstractThreadedSyncAdapter {

    /**
     * Intent extra to specify what to sync.
     */
    public static final String EXTRA_SYNC = "com.healthpole.radiologist.sync.syncDownloader";

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     * <p>
     * <p>
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to perform blocking I/O here.
     * <p>
     * <p>
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        DebugLog.d(">>>> Resources' sync started");

        if (Constant.IS_DEMO) {
            DebugLog.d("Deliberately not syncing as the app is in demo mode");
            DebugLog.d("<<<< Resources' sync terminated!");
            return;
        }
        /*if (account != null) {
            DebugLog.w("Deliberately not syncing as the APIs are not available on the new server");
            NetworkDownloader.context = getContext();
            NetworkDownloader.sendBroadcastToFragment(null);
            deviceInfoUploader.uploadInfo();
            return;
        }*/

        String downloader = extras.getString(EXTRA_SYNC);
        Context context = getContext();
        if (AppUtils.isEmpty(downloader)) {
            // write database sync here.
            // new NewsFeedUploader(context).upload();
            // new NewsFeedDownloader().download(context);
            // TODO: 12/6/17 Please add send broadcast here
        } else {
            /*switch (downloader) {
                case SyncUtils.INTENT_SYNC_NEWS_FEEDS:
                    new NewsFeedUploader(context).upload();
                    new NewsFeedDownloader().download(context);
                    break;

                case SyncUtils.INTENT_DELETE_NEWS_FEED_ITEMS:
                    new NewsFeedUploader(context).upload();
                    break;

                default:
                    break;
            }*/
        }

        DebugLog.d("<<<< Resources' sync completed!");
        // write data sanity checks at the end.
    }

    @Override
    public void onSyncCanceled() {
        super.onSyncCanceled();
        DebugLog.d("");
    }

    @Override
    public void onSecurityException(Account account, Bundle extras, String authority, SyncResult syncResult) {
        super.onSecurityException(account, extras, authority, syncResult);
        DebugLog.e("Extras: " + extras);
    }
}
