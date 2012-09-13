package org.solovyev.android.samples;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

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

    @SmallTest
    public void testList() throws Exception {
        assertTrue(getActivity().getListAdapter().getCount() >= 4);
    }
}
