package org.solovyev.android.samples.prefs;

import android.content.Context;
import android.util.AttributeSet;
import org.solovyev.android.prefs.AbstractEnumPickerDialogPreference;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 2:07 AM
 */
public class CountryPickerDialogPreference extends AbstractEnumPickerDialogPreference<Country> {

    public CountryPickerDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs, null, false, Country.class);
    }
}
