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

package org.solovyev.android.view.scroll;

import android.os.Bundle;
import android.widget.ScrollView;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * User: serso
 * Date: 8/5/12
 * Time: 2:09 AM
 */
public class ScrollViewState implements Serializable {

	@Nonnull
	private static final String SCROLL_VIEW_STATE = "scroll_view_state";

	private int scrollX = 0;

	private int scrollY = 0;

	public ScrollViewState() {
	}

	public ScrollViewState(@Nonnull ScrollView scrollView) {
		this.scrollX = scrollView.getScrollX();
		this.scrollY = scrollView.getScrollY();
	}

	public void restoreState(@Nonnull final ScrollView scrollView) {
		scrollView.post(new Runnable() {
			@Override
			public void run() {
				scrollView.scrollTo(scrollX, scrollY);
			}
		});

	}

	public static void saveState(@Nonnull Bundle out, @Nonnull final ScrollView scrollView) {
		out.putSerializable(SCROLL_VIEW_STATE, new ScrollViewState(scrollView));
	}

	public static void restoreState(@Nonnull Bundle in, @Nonnull final ScrollView scrollView) {
		final Object o = in.getSerializable(SCROLL_VIEW_STATE);
		if (o instanceof ScrollViewState) {
			((ScrollViewState) o).restoreState(scrollView);
		}
	}
}
