package org.solovyev.android;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

/**
 * User: serso
 * Date: 4/29/12
 * Time: 9:53 PM
 */
public final class DateVersionedEntityImpl<I> implements DateVersionedEntity<I> {

    @NotNull
    private VersionedEntity<I> versionedEntity;

    @NotNull
    private DateTime creationDate;

    @NotNull
    private DateTime modificationDate;

    private DateVersionedEntityImpl() {
    }

    @NotNull
    public static <I> DateVersionedEntity<I> newEntity(@NotNull I id) {
        final DateVersionedEntityImpl<I> result = new DateVersionedEntityImpl<I>();

        result.versionedEntity = new VersionedEntityImpl<I>(id);
        result.creationDate = DateTime.now();
        result.modificationDate = result.creationDate;

        return result;
    }

    @NotNull
    public static <I> DateVersionedEntity<I> newVersion(@NotNull DateVersionedEntity<I> dateVersionedEntity) {
        final DateVersionedEntityImpl<I> result = new DateVersionedEntityImpl<I>();

        // increase version
        result.versionedEntity = new VersionedEntityImpl<I>(dateVersionedEntity.getId(), dateVersionedEntity.getVersion() + 1);
        result.creationDate = dateVersionedEntity.getCreationDate();
        result.modificationDate = DateTime.now();

        return result;
    }

    @NotNull
    public static <I> DateVersionedEntity<I> newInstance(@NotNull VersionedEntity<I> versionedEntity, @NotNull DateTime creationDate, @NotNull DateTime modificationDate) {
        final DateVersionedEntityImpl<I> result = new DateVersionedEntityImpl<I>();

        result.versionedEntity = versionedEntity;
        result.creationDate = creationDate;
        result.modificationDate = modificationDate;

        return result;
    }

    @NotNull
    @Override
    public DateTime getCreationDate() {
        return this.creationDate;
    }

    @NotNull
    @Override
    public DateTime getModificationDate() {
        return this.modificationDate;
    }

    @Override
    @NotNull
    public I getId() {
        return versionedEntity.getId();
    }

    @Override
    @NotNull
    public Integer getVersion() {
        return versionedEntity.getVersion();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DateVersionedEntityImpl)) return false;

        DateVersionedEntityImpl that = (DateVersionedEntityImpl) o;

        if (!versionedEntity.equals(that.versionedEntity)) return false;

        return true;
    }

    @Override
    public boolean equalsVersion(Object that) {
        return this.equals(that) && this.versionedEntity.equalsVersion(((DateVersionedEntityImpl) that).versionedEntity);
    }

    @Override
    public int hashCode() {
        return versionedEntity.hashCode();
    }

    @NotNull
    @Override
    public DateVersionedEntityImpl<I> clone() {
        final DateVersionedEntityImpl<I> clone;

        try {
            //noinspection unchecked
            clone = (DateVersionedEntityImpl<I>) super.clone();

            clone.versionedEntity = this.versionedEntity.clone();

            // dates are immutable => can leave links as is

        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }

        return clone;
    }
}
