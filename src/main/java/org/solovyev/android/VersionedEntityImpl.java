package org.solovyev.android;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 4/29/12
 * Time: 9:47 PM
 */
public final class VersionedEntityImpl implements VersionedEntity {

    @NotNull
    public static final Integer FIRST_VERSION = 1;

    @NotNull
    private Integer id;

    @NotNull
    private Integer version = FIRST_VERSION;

    public VersionedEntityImpl(@NotNull Integer id) {
        this.id = id;
    }

    public VersionedEntityImpl(@NotNull Integer id, @NotNull Integer version) {
        this.id = id;
        this.version = version;
    }

    public VersionedEntityImpl(@NotNull VersionedEntity versionedEntity) {
        this.id = versionedEntity.getId();
        this.version = versionedEntity.getVersion();
    }

    @NotNull
    public static VersionedEntity newVersion(@NotNull VersionedEntity versionedEntity) {
        return new VersionedEntityImpl(versionedEntity.getId(),  versionedEntity.getVersion() + 1);
    }

    @NotNull
    @Override
    public Integer getId() {
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
        if (o == null || getClass() != o.getClass()) return false;

        VersionedEntityImpl that = (VersionedEntityImpl) o;

        if (!id.equals(that.id)) return false;

        return true;
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
}
