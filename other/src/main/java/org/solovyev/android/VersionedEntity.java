package org.solovyev.android;

import org.jetbrains.annotations.NotNull;
import org.solovyev.common.JCloneable;

import java.io.Serializable;

/**
 * User: serso
 * Date: 4/29/12
 * Time: 9:45 PM
 */
public interface VersionedEntity<I> extends Serializable, JCloneable<VersionedEntity<I>> {

    @NotNull
    I getId();

    @NotNull
    Integer getVersion();

    // check if two entities are the same == this.id equals to that.id
    boolean equals(Object that);

    // check if two entities are the same version == this.id equals to that.id && this.version equals to that.version
    boolean equalsVersion(Object that);
}
