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

package org.solovyev.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 2/23/13
 * Time: 2:35 PM
 */
public class DialogFragmentShower {

	@Nonnull
	private final FragmentActivity fragmentActivity;

	@Nonnull
	private final String fragmentTag;

	@Nonnull
	private final AlertDialog.Builder menuBuilder;

	public DialogFragmentShower(@Nonnull FragmentActivity fragmentActivity,
								@Nonnull String fragmentTag,
								@Nonnull AlertDialog.Builder menuBuilder) {
		this.fragmentActivity = fragmentActivity;
		this.fragmentTag = fragmentTag;
		this.menuBuilder = menuBuilder;
	}

	public void show() {
		show(true);
	}

	public void show(boolean addToBackStack) {
		if (addToBackStack) {
			Fragments.showDialog(new AlertDialogFragment(menuBuilder), fragmentTag, fragmentActivity.getSupportFragmentManager());
		} else {
			final FragmentManager fm = fragmentActivity.getSupportFragmentManager();
			final AlertDialogFragment fragment = new AlertDialogFragment(menuBuilder);
			fragment.show(fm, fragmentTag);
		}
	}

	/**
	 * Dirty solution for recreated dialog fragments - dismiss it after showing...
	 */
	public static final class AlertDialogFragment extends android.support.v4.app.DialogFragment {

		@Nullable
		private final AlertDialog.Builder menuBuilder;

		private AlertDialogFragment(@Nonnull AlertDialog.Builder menuBuilder) {
			this.menuBuilder = menuBuilder;
		}

		// for auto instantiation
		public AlertDialogFragment() {
			this.menuBuilder = null;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			if (menuBuilder == null) {
				dismiss();
			}
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			if (menuBuilder != null) {
				return menuBuilder.create();
			} else {
				return super.onCreateDialog(savedInstanceState);
			}
		}
	}
}
