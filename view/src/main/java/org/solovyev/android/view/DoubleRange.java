package org.solovyev.android.view;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 12:51 AM
 */
public class DoubleRange extends NumberRange<Double> {

    public DoubleRange(@NotNull Double min,
                       @NotNull Double max,
                       @NotNull Double step,
                       int startPosition) {
        super(Double.class, min, max, step, startPosition);
    }

    @NotNull
    public static NumberRange<Double> newInstance(@NotNull Double min, @NotNull Double max, @NotNull Double step, @NotNull Double selected) {
        if (selected < min || selected > max) {
            throw new IllegalArgumentException("Selected value: " + selected + " should be >= " + min + " and <= " + max + "!");
        }

        int startPosition = 0;
        for ( double i = min; i < selected; i += step ) {
            startPosition += 1;
        }

        return new DoubleRange(min, max, step, startPosition);
    }

    @Override
    protected int getCount(@NotNull Double min, @NotNull Double max, @NotNull Double step) {
        int result = (int) ((max - min) / step);
        return result + 1;
    }

    @NotNull
    @Override
    protected Double getValueAt(int position, @NotNull Double min, @NotNull Double max, @NotNull Double step) {
        return min + position * step;
    }
}
