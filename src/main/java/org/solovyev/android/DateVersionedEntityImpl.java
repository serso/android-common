package org.solovyev.android;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

/**
 * User: serso
 * Date: 4/29/12
 * Time: 9:53 PM
 */
public final class DateVersionedEntityImpl implements DateVersionedEntity {

    @NotNull
    private VersionedEntity versionedEntity;

    @NotNull
    private DateTime creationDate;

    @NotNull
    private DateTime modificationDate;

    private DateVersionedEntityImpl() {
    }

    @NotNull
    public static DateVersionedEntity newEntity(@NotNull Integer id) {
        final DateVersionedEntityImpl result = new DateVersionedEntityImpl();

        result.versionedEntity = new VersionedEntityImpl(id);
        result.creationDate = DateTime.now();
        result.modificationDate = result.creationDate;

        return result;
    }

    @NotNull
    public static DateVersionedEntity newVersion(@NotNull DateVersionedEntity dateVersionedEntity) {
        final DateVersionedEntityImpl result = new DateVersionedEntityImpl();

        // increase version
        result.versionedEntity = new VersionedEntityImpl(dateVersionedEntity.getId(), dateVersionedEntity.getVersion() + 1);
        result.creationDate = dateVersionedEntity.getCreationDate();
        result.modificationDate = DateTime.now();

        return result;
    }

    @NotNull
    public static DateVersionedEntity newInstance(@NotNull VersionedEntity versionedEntity, @NotNull DateTime creationDate, @NotNull DateTime modificationDate) {
        final DateVersionedEntityImpl result = new DateVersionedEntityImpl();

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
    public Integer getId() {
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
        if (o == null || getClass() != o.getClass()) return false;

        DateVersionedEntityImpl that = (DateVersionedEntityImpl) o;

        if (!versionedEntity.equals(that.versionedEntity)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return versionedEntity.hashCode();
    }

    @NotNull
    @Override
    public DateVersionedEntityImpl clone() {
        final DateVersionedEntityImpl clone;

        try {
            clone = (DateVersionedEntityImpl) super.clone();

            clone.versionedEntity = this.versionedEntity.clone();

            // dates are immutable => can leave links as is

        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }

        return clone;
    }
}
