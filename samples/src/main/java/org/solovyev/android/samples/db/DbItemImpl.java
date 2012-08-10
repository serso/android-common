package org.solovyev.android.samples.db;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 4:39 PM
 */
public class DbItemImpl implements DbItem {

    @NotNull
    private String name;

    public DbItemImpl(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    @Override
    public String getName() {
        return this.name;
    }
}
