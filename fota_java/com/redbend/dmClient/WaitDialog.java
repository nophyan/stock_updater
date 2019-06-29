/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.app.ProgressDialog
 *  android.content.Context
 *  android.view.Window
 */
package com.redbend.dmClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

public final class WaitDialog {
    private static ProgressDialog mProgressDialog;

    public static void dismiss() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

    public static void show(Context context, String string2) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
        }
        mProgressDialog.setProgress(0);
        mProgressDialog.getWindow().setType(2003);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage((CharSequence)string2);
        mProgressDialog.show();
    }
}

