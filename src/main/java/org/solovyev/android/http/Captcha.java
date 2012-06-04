package org.solovyev.android.http;

import android.os.Parcel;
import android.os.Parcelable;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 5/28/12
 * Time: 11:37 PM
 */
public class Captcha implements Parcelable {

    @NotNull
    private final String captchaSid;

    @NotNull
    private final String captchaImage;

    public Captcha(@NotNull String captchaSid, @NotNull String captchaImage) {
        this.captchaSid = captchaSid;
        this.captchaImage = captchaImage;
    }

    @NotNull
    public String getCaptchaSid() {
        return captchaSid;
    }

    @NotNull
    public String getCaptchaImage() {
        return captchaImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(captchaSid);
        out.writeString(captchaImage);
    }

    @NotNull
    public ResolvedCaptcha resolve(@NotNull String captchaKey) {
        return new ResolvedCaptcha(captchaSid, captchaKey);
    }
}
