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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DbItemImpl)) return false;

        DbItemImpl dbItem = (DbItemImpl) o;

        if (!name.equals(dbItem.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
