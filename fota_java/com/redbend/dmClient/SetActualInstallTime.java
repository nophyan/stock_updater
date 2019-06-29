/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 *  android.os.Bundle
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.widget.Button
 *  android.widget.EditText
 *  android.widget.NumberPicker
 */
package com.redbend.dmClient;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcApplication;
import com.redbend.dmcFramework.DmcLog;
import com.redbend.dmcFramework.TimeUtils;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class SetActualInstallTime
extends DmcActivity {
    private final String LOG_TAG = "SetActualInstallTime";
    protected DmcApplication a;
    private int dHH;
    private int dMM;
    private NumberPicker np;
    private String[] timeSlots;

    /*
     * Enabled aggressive block sorting
     */
    private EditText findEditText(ViewGroup viewGroup) {
        int n = viewGroup.getChildCount();
        int n2 = 0;
        while (n2 < n) {
            View view = viewGroup.getChildAt(n2);
            if (view instanceof ViewGroup) {
                this.findEditText((ViewGroup)view);
            } else if (view instanceof EditText) {
                return (EditText)view;
            }
            ++n2;
        }
        return null;
    }

    public native int getDefaultInstallSchedHh();

    public native int getDefaultInstallSchedMm();

    @Override
    public void onCreate(Bundle bundle) {
        DmcLog.v("SetActualInstallTime", "onCreate()");
        super.onCreate(bundle);
        this.setContentView(2130903060);
        this.a = (DmcApplication)this.getApplication();
        this.np = (NumberPicker)this.findViewById(2131230736);
        this.timeSlots = TimeUtils.getTimeSlotsOfDay(this.mContext);
        this.dHH = TimeUtils.getInstallScheduleHour(this.mContext);
        this.dMM = TimeUtils.getInstallScheduleMinute(this.mContext);
        this.np.setDisplayedValues(this.timeSlots);
        this.np.setMinValue(0);
        this.np.setMaxValue(this.timeSlots.length - 1);
        this.np.setValue(TimeUtils.getTimeSlotIndex(this.timeSlots, this.dHH, this.dMM));
        this.np.setOnLongPressUpdateInterval(200);
        this.np.setWrapSelectorWheel(true);
        bundle = this.findEditText((ViewGroup)this.np);
        if (bundle != null) {
            bundle.setInputType(0);
            bundle.setFocusable(false);
            bundle.setClickable(false);
            bundle.clearFocus();
        }
    }

    @Override
    public void setContentView(int n) {
        DmcLog.v("SetActualInstallTime", "setContentView()");
        super.setContentView(n);
        final Button button = (Button)this.findViewById(2131230723);
        button.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                SetActualInstallTime.this.dHH = TimeUtils.getHrFromIndex(SetActualInstallTime.this.timeSlots, SetActualInstallTime.this.np.getValue());
                SetActualInstallTime.this.dMM = TimeUtils.getMinFromIndex(SetActualInstallTime.this.timeSlots, SetActualInstallTime.this.np.getValue());
                TimeUtils.setInstallScheduleTime(SetActualInstallTime.this.mContext, SetActualInstallTime.this.dHH, SetActualInstallTime.this.dMM);
                button.setClickable(false);
                SetActualInstallTime.this.a.sendMessage(2, view.getTag().toString());
            }
        });
    }

}

