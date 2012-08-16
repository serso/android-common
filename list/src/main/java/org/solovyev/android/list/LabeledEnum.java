package org.solovyev.android.list;

import android.content.Context;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.Labeled;

import java.util.ArrayList;
import java.util.List;

/**
 * User: serso
 * Date: 7/31/12
 * Time: 11:36 PM
 */
public class LabeledEnum<E> {

    @NotNull
    private E enumConstant;

    @NotNull
    private String label;

    private LabeledEnum() {
    }

    @NotNull
    public static <E> LabeledEnum<E> newInstance(@NotNull E enumConstant, @NotNull String label) {
        final LabeledEnum<E> result = new LabeledEnum<E>();

        result.enumConstant = enumConstant;
        result.label = label;

        return result;
    }

    @NotNull
    public static <E extends Enum & Labeled> LabeledEnum<E> newInstance(@NotNull E enumConstant,
                                                                        @NotNull Context context) {
        return LabeledEnum.newInstance(enumConstant, context.getString(enumConstant.getCaptionResId()));
    }

    @NotNull
    public static <E extends Enum & Labeled> List<LabeledEnum<E>> toLabeledEnums(@NotNull Class<E> enumClass,
                                                                                 @NotNull Context context) {
        final E[] enumConstants = enumClass.getEnumConstants();

        final List<LabeledEnum<E>> result = new ArrayList<LabeledEnum<E>>(enumConstants.length);
        for (E enumConstant : enumConstants) {
            result.add(LabeledEnum.newInstance(enumConstant, context));
        }

        return result;
    }

    @NotNull
    public E getEnumConstant() {
        return enumConstant;
    }

    @NotNull
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LabeledEnum)) return false;

        LabeledEnum that = (LabeledEnum) o;

        if (!enumConstant.equals(that.enumConstant)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return enumConstant.hashCode();
    }
}
