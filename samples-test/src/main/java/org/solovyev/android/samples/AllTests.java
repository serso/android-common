package org.solovyev.android.samples;

import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * User: serso
 * Date: 9/13/12
 * Time: 11:58 PM
 */
public class AllTests extends TestSuite {

    public static Test suite() {
        return new TestSuiteBuilder(AllTests.class)
                .includeAllPackagesUnderHere()
                .build();
    }
}
