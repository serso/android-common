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
import org.solovyev.android.Labeled;

import javax.annotation.Nonnull;
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

	@Nonnull
	public static <E extends Enum & Labeled> ListAdapter<LabeledEnum<E>> newDefaultAdapterForEnum(@Nonnull Context context,
																								  @Nonnull Class<E> enumClass) {
		final List<LabeledEnum<E>> labeledEnums = LabeledEnum.toLabeledEnums(enumClass, context);

		return newDefaultAdapter(context, labeledEnums);
	}

	@Nonnull
	public static <T> ListAdapter<T> newDefaultAdapter(@Nonnull Context context,
													   @Nonnull List<T> items) {

		final ListAdapter<T> result = new ListAdapter<T>(context, android.R.layout.simple_spinner_item, items);
		result.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return result;
	}
}
