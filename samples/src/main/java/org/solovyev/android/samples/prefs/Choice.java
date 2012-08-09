package org.solovyev.android.samples.prefs;

import org.solovyev.android.Labeled;
import org.solovyev.android.samples.R;

/**
* User: serso
* Date: 8/8/12
* Time: 2:56 AM
*/
public enum Choice implements Labeled {

    to_be(R.string.to_be),
    not_to_be(R.string.not_to_be);

    private final int captionResId;

    Choice(int captionResId) {
        this.captionResId = captionResId;
    }


    @Override
    public int getCaptionResId() {
        return this.captionResId;
    }
}
