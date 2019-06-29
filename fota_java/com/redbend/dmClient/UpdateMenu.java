/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.view.KeyEvent
 *  android.view.View
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.ListView
 */
package com.redbend.dmClient;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcApplication;
import com.redbend.dmcFramework.DmcLog;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class UpdateMenu
extends DmcActivity {
    @Override
    public void onCreate(Bundle bundle) {
        this.LOG_TAG = "UpdateMenu";
        DmcLog.v(this.LOG_TAG, "onCreate()");
        super.onCreate(bundle);
        this.setContentView(2130903068);
    }

    public boolean onKeyUp(int n, KeyEvent keyEvent) {
        if (n == 4) {
            DmcLog.d(this.LOG_TAG, "back button pressed, move to back");
            this.moveTaskToBack(true);
            return false;
        }
        return super.onKeyUp(n, keyEvent);
    }

    @Override
    public void setContentView(int n) {
        DmcLog.v(this.LOG_TAG, "setContentView()");
        super.setContentView(n);
        ListView listView = (ListView)this.findViewById(2131230726);
        listView.setChoiceMode(1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> adapterView, View view, int n, long l) {
                UpdateMenu.this.a.sendMessage(17, n);
            }
        });
    }

}

