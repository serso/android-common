package org.solovyev.android.samples.prefs;

import org.solovyev.android.Labeled;
import org.solovyev.android.samples.R;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 2:09 AM
 */
public enum Country implements Labeled {

    russia(R.string.russia),
    ukraine(R.string.ukraine),
    usa(R.string.usa),
    sweden(R.string.sweden),
    germany(R.string.germany);

    private final int captionResId;

    private Country(int captionResId) {
        this.captionResId = captionResId;
    }


    @Override
    public int getCaptionResId() {
        return this.captionResId;
    }
}
