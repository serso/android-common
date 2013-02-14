package org.solovyev.android;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public enum APhoneModel {

    samsung_galaxy_s_2(	"GT-I9100","GT-I9100M","GT-I9100P","GT-I9100T","SC-02C","SHW-M250K","SHW-M250L","SHW-M250S"),
    samsung_galaxy_s("GT-I9000","GT-I9000B","GT-I9000M","GT-I9000T","SGH-I897");

    @NotNull
    private final List<String> models;

    APhoneModel(@NotNull String... models) {
        this.models = Arrays.asList(models);
    }

    @NotNull
    public List<String> getModels() {
        return models;
    }
}
