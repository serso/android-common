/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.list;

import android.content.Context;
import javax.annotation.Nonnull;
import org.solovyev.android.Labeled;

import java.util.ArrayList;
import java.util.List;

/**
 * User: serso
 * Date: 7/31/12
 * Time: 11:36 PM
 */
public class LabeledEnum<E> {

    @Nonnull
    private E enumConstant;

    @Nonnull
    private String label;

    private LabeledEnum() {
    }

    @Nonnull
    public static <E> LabeledEnum<E> newInstance(@Nonnull E enumConstant, @Nonnull String label) {
        final LabeledEnum<E> result = new LabeledEnum<E>();

        result.enumConstant = enumConstant;
        result.label = label;

        return result;
    }

    @Nonnull
    public static <E extends Enum & Labeled> LabeledEnum<E> newInstance(@Nonnull E enumConstant,
                                                                        @Nonnull Context context) {
        return LabeledEnum.newInstance(enumConstant, context.getString(enumConstant.getCaptionResId()));
    }

    @Nonnull
    public static <E extends Enum & Labeled> List<LabeledEnum<E>> toLabeledEnums(@Nonnull Class<E> enumClass,
                                                                                 @Nonnull Context context) {
        final E[] enumConstants = enumClass.getEnumConstants();

        final List<LabeledEnum<E>> result = new ArrayList<LabeledEnum<E>>(enumConstants.length);
        for (E enumConstant : enumConstants) {
            result.add(LabeledEnum.newInstance(enumConstant, context));
        }

        return result;
    }

    @Nonnull
    public E getEnumConstant() {
        return enumConstant;
    }

    @Nonnull
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
