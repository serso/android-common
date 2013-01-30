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

package org.solovyev.android.samples;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.widget.ListAdapter;

/**
 * User: serso
 * Date: 9/13/12
 * Time: 11:12 PM
 */
public class SamplesActivityTest extends ActivityInstrumentationTestCase2<SamplesActivity> {

    public SamplesActivityTest() {
        super(SamplesActivity.class.getPackage().getName(), SamplesActivity.class);
    }

    @SmallTest
    public void testPreconditions() throws Exception {
    }

    @UiThreadTest
    public void testList() throws Exception {
        final SamplesActivity activity = getActivity();
        final ListAdapter adapter = activity.getListAdapter();
        if (adapter != null) {
            assertTrue(adapter.getCount() >= 4);
        } else {
            Log.w("Test", "List adapter is null, must be fixed!");
        }
    }
}
