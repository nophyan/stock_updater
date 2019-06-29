/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.view.KeyEvent
 *  android.view.View
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.AdapterView$OnItemSelectedListener
 *  android.widget.ArrayAdapter
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.TextView
 */
package com.redbend.dmcFramework;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcApplication;
import com.redbend.dmcFramework.DmcLog;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class DmcChoiceListQuery
extends DmcActivity {
    static final int headerInfomationFontSize = 18;
    private String LOG_TAG;
    private DmcApplication a;
    private int defaultSelection;
    private String displayText;
    private boolean isMultiSelect;
    private String[] listItems = null;
    private String[] listItemsReference = null;
    private int selectedItemIndex = 0;

    private void addNumbersToItems(String[] arrstring) {
        for (int i = 0; i < arrstring.length; ++i) {
            arrstring[i] = String.valueOf(i + 1) + "." + arrstring[i];
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        this.LOG_TAG = "DmcChoiceListQuery";
        DmcLog.v(this.LOG_TAG, "onCreate()");
        super.onCreate(bundle);
        this.a = (DmcApplication)this.getApplication();
        this.displayText = this.getIntent().getBundleExtra("uiAlertBundleExtra").getString("displayText");
        this.isMultiSelect = this.getIntent().getBundleExtra("uiAlertBundleExtra").getBoolean("isMultiSelect");
        this.defaultSelection = this.getIntent().getBundleExtra("uiAlertBundleExtra").getInt("defaultSelection", 1);
        this.listItemsReference = this.getIntent().getBundleExtra("uiAlertBundleExtra").getStringArray("items");
        if (this.listItems == null) {
            DmcLog.d(this.LOG_TAG, "hard copy");
            this.listItems = new String[this.listItemsReference.length];
            for (int i = 0; i < this.listItemsReference.length; ++i) {
                this.listItems[i] = new String(this.listItemsReference[i]);
            }
        }
        this.addNumbersToItems(this.listItems);
        this.setContentView(2130903044);
    }

    @Override
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (n == 4) {
            DmcLog.d(this.LOG_TAG, "back button pressed, ignore");
            return false;
        }
        return super.onKeyDown(n, keyEvent);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setContentView(int n) {
        DmcLog.d(this.LOG_TAG, "setContentView()");
        super.setContentView(n);
        ListView listView = (ListView)this.findViewById(2131230726);
        if (this.isMultiSelect) {
            listView.setChoiceMode(2);
        } else {
            listView.setChoiceMode(1);
        }
        if (this.displayText != null && this.displayText.length() > 0) {
            TextView textView = new TextView((Context)this);
            textView.setText((CharSequence)this.displayText);
            textView.setGravity(3);
            textView.setTextSize(2, 18.0f);
            listView.addHeaderView((View)textView, (Object)null, false);
        }
        listView.setSelectionAfterHeaderView();
        listView.setSelection(this.defaultSelection);
        listView.setAdapter((ListAdapter)new ArrayAdapter((Context)this, 2130903043, 2131230720, this.listItems));
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            public void onItemSelected(AdapterView<?> adapterView, View view, int n, long l) {
                DmcChoiceListQuery.this.selectedItemIndex = n;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        listView.setSelection(this.selectedItemIndex);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> adapterView, View view, int n, long l) {
                DmcChoiceListQuery.this.a.sendMessage(8, n);
            }
        });
    }

}

