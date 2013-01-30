/*
 * Copyright (c) 2009-2011. Created by serso aka se.solovyev.
 * For more information, please, contact se.solovyev@gmail.com
 * or visit http://se.solovyev.org
 */

package org.solovyev.android.prefs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import org.jetbrains.annotations.NotNull;
import org.solovyev.common.text.NumberMapper;


/* The following code was written by Matthew Wiggins
 * and is released under the APACHE 2.0 license
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

public class SeekBarPreference extends AbstractDialogPreference<Integer> implements SeekBar.OnSeekBarChangeListener {

    private int max = 0;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs, "50", true, NumberMapper.of(Integer.class));

        max = attrs.getAttributeIntValue(androidns, "max", 100);
    }

    @Override
    protected LinearLayout.LayoutParams getParams() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @NotNull
    @Override
    protected View createPreferenceView(@NotNull Context context) {
        final SeekBar result = new SeekBar(context);

        result.setOnSeekBarChangeListener(this);

        return result;
    }

    @Override
    protected void initPreferenceView(@NotNull View v, Integer value) {
        ((SeekBar) v).setMax(max);
        if (value != null) {
            ((SeekBar) v).setProgress(value);
            setValueText(value);
        }
    }

    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
        setValueText(value);

        persistValue(value);
    }

    private void setValueText(int value) {
        String t = String.valueOf(value);
        final String valueText = getValueText();
        updateValueText(valueText == null ? t : t.concat(valueText));
    }

    public void onStartTrackingTouch(SeekBar seek) {
    }

    public void onStopTrackingTouch(SeekBar seek) {
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    public void setProgress(int progress) {
        setValue(progress);
        final View preferenceView = getPreferenceView();
        if (preferenceView != null) {
            ((SeekBar) preferenceView).setProgress(progress);
        }
    }

    public int getProgress() {
        final Integer value = getValue();
        return value == null ? 0 : value;
    }
}

