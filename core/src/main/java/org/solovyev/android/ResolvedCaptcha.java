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
 * ---------------------------------------------------------------------
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

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
