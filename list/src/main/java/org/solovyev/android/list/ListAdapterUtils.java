package org.solovyev.android.list;

import android.content.Context;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.Labeled;

import java.util.List;

/**
 * User: serso
 * Date: 8/13/12
 * Time: 2:22 AM
 */
public final class ListAdapterUtils {

    private ListAdapterUtils() {
        throw new AssertionError();
    }

    @NotNull
    public static <E extends Enum & Labeled> ListAdapter<LabeledEnum<E>> newDefaultAdapterForEnum(@NotNull Context context,
                                                                                                  @NotNull Class<E> enumClass) {
        final List<LabeledEnum<E>> labeledEnums = LabeledEnum.toLabeledEnums(enumClass, context);

        return newDefaultAdapter(context, labeledEnums);
    }

    @NotNull
    public static <T> ListAdapter<T> newDefaultAdapter(@NotNull Context context,
                                                       @NotNull List<T> items) {

        final ListAdapter<T> result = new ListAdapter<T>(context, android.R.layout.simple_spinner_item, items);
        result.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return result;
    }
}
