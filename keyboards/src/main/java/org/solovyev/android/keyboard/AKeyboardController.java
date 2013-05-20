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

package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodSubtype;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 8:07 PM
 */
public interface AKeyboardController {

    /*
	 **********************************************************************
     *
     *                           LIFECYCLE
     *
     **********************************************************************
     */

	/**
	 * Main initialization of the input method component.  Be sure to call
	 * to super class.
	 */
	void onCreate(@Nonnull Context context);

	/**
	 * This is the point where you can do all of your UI initialization.  It
	 * is called after creation and any configuration change.
	 */
	void onInitializeInterface(@Nonnull InputMethodService inputMethodService);

	/**
	 * Called by the framework when your view for creating input needs to
	 * be generated.  This will be called the first time your input method
	 * is displayed, and every time it needs to be re-created such as due to
	 * a configuration change.
	 */
	@Nonnull
	AKeyboardView createKeyboardView(@Nonnull Context context, @Nonnull LayoutInflater layoutInflater);

	/**
	 * Called by the framework when your view for showing candidates needs to
	 * be generated, like {@link #createKeyboardView}.
	 */
	@Nullable
	View onCreateCandidatesView();

	/**
	 * This is the main point where we do our initialization of the input method
	 * to begin operating on an application.  At this point we have been
	 * bound to the client, and are now receiving all of the detailed information
	 * about the target of our edits.
	 */
	void onStartInput(@Nonnull EditorInfo attribute, boolean restarting);

	/**
	 * This is called when the user is done editing a field.  We can use
	 * this to reset our state.
	 */
	void onFinishInput();

    /*
     **********************************************************************
     *
     *                           SYSTEM CALLS
     *
     **********************************************************************
     */

	/**
	 * Deal with the editor reporting movement of its cursor.
	 */
	void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd);

	/**
	 * This tells us about completions that the editor has determined based
	 * on the current text in it.  We want to use this in fullscreen mode
	 * to show the completions ourself, since the editor can not be seen
	 * in that situation.
	 */
	void onDisplayCompletions(@Nullable CompletionInfo[] completions);

	/**
	 * Use this to monitor key events being delivered to the application.
	 * We get first crack at them, and can either resume them or let them
	 * continue to the app.
	 */
	boolean onKeyDown(int keyCode, KeyEvent event);

	/**
	 * Use this to monitor key events being delivered to the application.
	 * We get first crack at them, and can either resume them or let them
	 * continue to the app.
	 */
	boolean onKeyUp(int keyCode, KeyEvent event);

	void onCurrentInputMethodSubtypeChanged(@Nonnull InputMethodSubtype subtype);

	void onStartInputView(EditorInfo attribute, boolean restarting);

    /*
     **********************************************************************
     *
     *                           OTHER
     *
     **********************************************************************
     */

	boolean onKey(int primaryCode, @Nullable int[] keyCodes);

	void handleClose();

	boolean handleBackspace();

	void onText(@Nullable CharSequence text);

	void pickDefaultCandidate();

	void pickSuggestionManually(int index);
}
