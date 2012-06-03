package org.solovyev.android;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * User: serso
 * Date: 4/29/12
 * Time: 9:45 PM
 */
public interface VersionedEntity extends Serializable {

    @NotNull
    Integer getId();

    @NotNull
    Integer getVersion();
}
