package org.solovyev.android.view.sidebar;

public interface OnSlideListener {

    /**
     * Called when slide action has been finished.
     *
     * @param opened true if sliding view has been opened, false - if closed
     */
    public void onSlideCompleted(boolean opened);
}
