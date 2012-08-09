package org.solovyev.android.view;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 12:08 AM
 */
public class IntegerRange extends NumberRange<Integer> {

    public IntegerRange(@NotNull Integer min,
                        @NotNull Integer max,
                        @NotNull Integer step,
                        int startPosition) {
        super(Integer.class, min, max, step, startPosition);
    }

    @NotNull
    public static NumberRange<Integer> newInstance(@NotNull Integer min, @NotNull Integer max, @NotNull Integer step, @NotNull Integer selected) {
        if (selected < min || selected > max) {
            throw new IllegalArgumentException("Selected value: " + selected + " should be >= " + min + " and <= " + max + "!");
        }

        int startPosition = 0;
        for ( int i = min; i < selected; i += step ) {
            startPosition += 1;
        }

        return new IntegerRange(min, max, step, startPosition);
    }

    @Override
    protected int getCount(@NotNull Integer min, @NotNull Integer max, @NotNull Integer step) {
        // (4 - 0)/1 + 1= 5
        // (4 - 0)/2 + 1 = 3
        // (4 - 1)/2 + 1 = 2
        return (max - min) / step + 1;
    }

    @NotNull
    @Override
    protected Integer getValueAt(int position, @NotNull Integer min, @NotNull Integer max, @NotNull Integer step) {
        return min + position * step;
    }
}
