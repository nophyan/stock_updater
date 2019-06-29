/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.Button
 */
package com.redbend.dmcFramework;

import android.content.Context;
import android.view.View;
import android.widget.Button;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class DMCButton
extends Button {
    private Button button;
    private boolean canFireClick = true;
    private View.OnClickListener listener;

    public DMCButton(Button button) {
        super(button.getContext());
        this.button = button;
    }

    public void setClickable(boolean bl) {
        if (this.button != null) {
            this.button.setClickable(bl);
            this.canFireClick = true;
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.listener = onClickListener;
        this.button.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                if (DMCButton.this.canFireClick) {
                    DMCButton.this.listener.onClick(view);
                    DMCButton.this.canFireClick = false;
                }
            }
        });
    }

    public void setTag(Object object) {
        this.button.setTag(object);
    }

}

