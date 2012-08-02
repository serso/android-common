package org.solovyev.android;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 4/29/12
 * Time: 9:47 PM
 */
public final class VersionedEntityImpl<I> implements VersionedEntity<I> {

    @NotNull
    public static final Integer FIRST_VERSION = 1;

    @NotNull
    private I id;

    @NotNull
    private Integer version = FIRST_VERSION;

    public VersionedEntityImpl(@NotNull I id) {
        this.id = id;
    }

    public VersionedEntityImpl(@NotNull I id, @NotNull Integer version) {
        this.id = id;
        this.version = version;
    }

    public VersionedEntityImpl(@NotNull VersionedEntity<I> versionedEntity) {
        this.id = versionedEntity.getId();
        this.version = versionedEntity.getVersion();
    }

    @NotNull
    public static <I> VersionedEntity<I> newVersion(@NotNull VersionedEntity<I> versionedEntity) {
        return new VersionedEntityImpl<I>(versionedEntity.getId(),  versionedEntity.getVersion() + 1);
    }

    @NotNull
    @Override
    public I getId() {
        return this.id;
    }

    @NotNull
    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VersionedEntityImpl)) return false;

        VersionedEntityImpl that = (VersionedEntityImpl) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public boolean equalsVersion(Object that) {
        return this.equals(that) && this.version.equals(((VersionedEntityImpl) that).version);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "VersionedEntityImpl{" +
                "id=" + id +
                ", version=" + version +
                '}';
    }

    @NotNull
    @Override
    public VersionedEntityImpl<I> clone() {
        final VersionedEntityImpl<I> clone;

        try {
            //noinspection unchecked
            clone = (VersionedEntityImpl<I>)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }

        return clone;
    }
}
