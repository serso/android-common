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

package org.solovyev.android.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import org.solovyev.android.DialogFragmentShower;
import org.solovyev.common.Builder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 4/29/12
 * Time: 1:25 PM
 */
public class ConfirmationDialogBuilder implements Builder<DialogFragmentShower> {

    /*
	**********************************************************************
    *
    *                           FIELDS
    *
    **********************************************************************
    */

	@Nonnull
	private final FragmentActivity fragmentActivity;

	@Nonnull
	private final String fragmentTag;

	private final int messageResId;

	private int titleResId = R.string.c_confirmation;

	private int positiveButtonTextResId = android.R.string.ok;

	private int negativeButtonTextResId = android.R.string.cancel;

	@Nullable
	private DialogInterface.OnClickListener positiveHandler;

	@Nullable
	private DialogInterface.OnClickListener negativeHandler;

    /*
    **********************************************************************
    *
    *                           CONSTRUCTORS
    *
    **********************************************************************
    */

	private ConfirmationDialogBuilder(@Nonnull FragmentActivity fragmentActivity, @Nonnull String fragmentTag, int messageResId) {
		this.fragmentActivity = fragmentActivity;
		this.fragmentTag = fragmentTag;
		this.messageResId = messageResId;
	}

	@Nonnull
	public static ConfirmationDialogBuilder newInstance(@Nonnull FragmentActivity fragmentActivity, @Nonnull String fragmentTag, int messageResId) {
		return new ConfirmationDialogBuilder(fragmentActivity, fragmentTag, messageResId);
	}

    /*
    **********************************************************************
    *
    *                           METHODS
    *
    **********************************************************************
    */

	@Nonnull
	public ConfirmationDialogBuilder setTitleResId(int titleResId) {
		this.titleResId = titleResId;
		return this;
	}

	@Nonnull
	public ConfirmationDialogBuilder setPositiveButtonTextResId(int positiveButtonTextResId) {
		this.positiveButtonTextResId = positiveButtonTextResId;
		return this;
	}

	@Nonnull
	public ConfirmationDialogBuilder setNegativeButtonTextResId(int negativeButtonTextResId) {
		this.negativeButtonTextResId = negativeButtonTextResId;
		return this;
	}

	@Nonnull
	public ConfirmationDialogBuilder setPositiveHandler(@Nullable DialogInterface.OnClickListener positiveHandler) {
		this.positiveHandler = positiveHandler;
		return this;
	}

	@Nonnull
	public ConfirmationDialogBuilder setNegativeHandler(@Nullable DialogInterface.OnClickListener negativeHandler) {
		this.negativeHandler = negativeHandler;
		return this;
	}

	@Nonnull
	@Override
	public DialogFragmentShower build() {
		AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);

		builder.setTitle(titleResId);
		builder.setMessage(messageResId);
		builder.setPositiveButton(positiveButtonTextResId, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (positiveHandler != null) {
					positiveHandler.onClick(dialog, which);
				}
			}
		});

		builder.setNegativeButton(negativeButtonTextResId, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (negativeHandler != null) {
					negativeHandler.onClick(dialog, which);
				}
			}
		});

		return new DialogFragmentShower(fragmentActivity, fragmentTag, builder);
	}
}
