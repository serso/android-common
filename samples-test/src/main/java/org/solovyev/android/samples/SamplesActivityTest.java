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
