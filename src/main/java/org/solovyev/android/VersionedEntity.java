package org.solovyev.android;

import org.jetbrains.annotations.NotNull;
import org.solovyev.common.JCloneable;

import java.io.Serializable;

/**
 * User: serso
 * Date: 4/29/12
 * Time: 9:45 PM
 */
public interface VersionedEntity extends Serializable, JCloneable<VersionedEntity> {

    @NotNull
    Integer getId();

    @NotNull
    Integer getVersion();
}
