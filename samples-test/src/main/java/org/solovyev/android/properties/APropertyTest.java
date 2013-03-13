package org.solovyev.android.properties;

import android.os.Parcel;
import android.test.AndroidTestCase;
import junit.framework.Assert;

public class APropertyTest extends AndroidTestCase {

    public void testParcelable() throws Exception {
        AProperty expected = Properties.newProperty("test_name", "test_value");

        Parcel parcel = Parcel.obtain();
        expected.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        AProperty actual = APropertyImpl.CREATOR.createFromParcel(parcel);
        Assert.assertEquals(expected, actual);

        parcel = Parcel.obtain();
        expected = Properties.newProperty("test_name", null);
        expected.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        actual = APropertyImpl.CREATOR.createFromParcel(parcel);
        Assert.assertEquals(expected, actual);

    }
}
