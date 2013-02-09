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

import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.robotmedia.billing.utils.ObfuscateUtilsTest;
import org.solovyev.android.prefs.AbstractPreferenceTest;
import org.solovyev.android.security.AndroidStringCiphererTest;

/**
 * User: serso
 * Date: 9/13/12
 * Time: 11:58 PM
 */
public class AllTests extends TestSuite {

    public static Test suite() {
        return new TestSuiteBuilder(AllTests.class)
                .includeAllPackagesUnderHere()
                .includePackages(ObfuscateUtilsTest.class.getPackage().getName())
                .includePackages(AndroidStringCiphererTest.class.getPackage().getName())
                .includePackages(AbstractPreferenceTest.class.getPackage().getName())
                .build();
    }
}
