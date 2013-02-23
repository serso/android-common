package org.solovyev.android.db;

import android.database.Cursor;
import org.jetbrains.annotations.NotNull;
import org.solovyev.common.Converter;
import org.solovyev.common.VersionedEntity;
import org.solovyev.common.VersionedEntityImpl;

/**
 * User: serso
 * Date: 7/25/12
 * Time: 2:24 PM
 */
public class StringVersionedEntityMapper implements Converter<Cursor, VersionedEntity<String>> {

    @NotNull
    private static final Converter<Cursor, VersionedEntity<String>> instance = new StringVersionedEntityMapper();

    private StringVersionedEntityMapper() {
    }

    @NotNull
    public static Converter<Cursor, VersionedEntity<String>> getInstance() {
        return instance;
    }

    @NotNull
    @Override
    public VersionedEntity<String> convert(@NotNull Cursor cursor) {

        final String id = cursor.getString(0);
        final int version = cursor.getInt(1);

        return new VersionedEntityImpl<String>(id, version);
    }
}
