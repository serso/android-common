package org.solovyev.android;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 5/28/12
 * Time: 11:26 PM
 */
public class ResolvedCaptcha {

    @NotNull
    private final String captchaSid;

    @NotNull
    private final String captchaKey;

    public ResolvedCaptcha(@NotNull String captchaSid, @NotNull String captchaKey) {
        this.captchaSid = captchaSid;
        this.captchaKey = captchaKey;
    }

    @NotNull
    public String getCaptchaSid() {
        return captchaSid;
    }

    @NotNull
    public String getCaptchaKey() {
        return captchaKey;
    }
}
