package org.solovyev.android.prefs;

import android.content.Context;
import android.util.AttributeSet;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.view.DoubleRange;
import org.solovyev.android.view.NumberRange;
import org.solovyev.common.text.NumberIntervalMapper;
import org.solovyev.common.utils.Interval;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 1:02 AM
 */
public class DoublePickerDialogPreference extends NumberPickerDialogPreference<Double> {

    @NotNull
    @Override
    protected Double getDefaultStep() {
        return 1d;
    }

    @NotNull
    @Override
    protected NumberRange<Double> createRange(@NotNull Interval<Double> boundaries, @NotNull Double step, @NotNull Double selected) {
        return DoubleRange.newInstance(boundaries.getLeftLimit(), boundaries.getRightLimit(), step, selected);
    }

    public DoublePickerDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs, new NumberIntervalMapper<Double>(Double.class));
    }
}
