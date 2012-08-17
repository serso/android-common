package org.solovyev.android.http;

import android.graphics.Bitmap;
import org.jetbrains.annotations.Nullable;

/**
* User: serso
* Date: 8/17/12
* Time: 12:06 PM
*/
public interface OnImageLoadedListener {

    // NOTE: this method will be called on the background thread => if needed use runOnUIThread
    void onImageLoaded(@Nullable Bitmap image);

    // NOTE: this method will be called on the background thread => if needed use runOnUIThread
    void setDefaultImage();

}
