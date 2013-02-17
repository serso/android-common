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

package org.solovyev.android;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public enum DeviceModel {

    samsung_galaxy_s_2(	"GT-I9100","GT-I9100M","GT-I9100P","GT-I9100T","SC-02C","SHW-M250K","SHW-M250L","SHW-M250S"),
    samsung_galaxy_s("GT-I9000","GT-I9000B","GT-I9000M","GT-I9000T","SGH-I897");

    @NotNull
    private final List<String> models;

    DeviceModel(@NotNull String... models) {
        this.models = Arrays.asList(models);
    }

    @NotNull
    public List<String> getModels() {
        return models;
    }
}
