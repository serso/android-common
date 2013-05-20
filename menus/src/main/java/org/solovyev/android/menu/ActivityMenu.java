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

package org.solovyev.android.menu;

import android.app.Activity;

import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 4/23/12
 * Time: 1:56 PM
 */

/**
 * Menu for activity.
 * Note: this class is parametrised by Menu and MenuItem for support menu from ActionSherlockBar library
 *
 * @param <M>  menu type (may differ from android.view.Menu, e.g. in Android Sherlock Library)
 * @param <MI> menu item type (may differ from android.view.MenuItem, e.g. in Android Sherlock Library)
 */
public interface ActivityMenu<M, MI> {

	boolean onCreateOptionsMenu(@Nonnull Activity activity, @Nonnull M menu);

	boolean onPrepareOptionsMenu(@Nonnull Activity activity, @Nonnull M menu);

	boolean onOptionsItemSelected(@Nonnull Activity activity, @Nonnull MI item);
}
