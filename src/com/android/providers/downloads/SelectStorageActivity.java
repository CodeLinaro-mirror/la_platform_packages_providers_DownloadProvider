/*
 * Copyright (c) 2013, The Linux Foundation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *     * Neither the name of The Linux Foundation nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.android.providers.downloads;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;

public class SelectStorageActivity extends Activity {
    private AlertDialog mDialog = null;

    @Override
    protected void onResume() {
        super.onResume();
        String downloadTitle = getIntent().getStringExtra("download_title");
        Resources res = getResources();
        mDialog = new AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(res.getString(R.string.select_storage_title, downloadTitle == null ? "" : downloadTitle))
            .setSingleChoiceItems(
                    new CharSequence[] {
                            res.getString(R.string.auto),
                            res.getString(R.string.internal_storage),
                            res.getString(R.string.external_storage)},
                    0,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startDownloadService(which);
                            mDialog.dismiss();
                            mDialog = null;
                            finish();
                        }
                    })
            .create();
        mDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    private void startDownloadService(int selectedStorage) {
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("action", "storage_selected_for_play_store");
        intent.putExtra("id", getIntent().getLongExtra("id", -1));
        intent.putExtra("selected_storage", selectedStorage);
        startService(intent);
    }
}
