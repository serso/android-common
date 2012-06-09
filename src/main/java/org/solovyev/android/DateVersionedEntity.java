package org.solovyev.android;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

/**
 * User: serso
 * Date: 4/29/12
 * Time: 9:52 PM
 */
public interface DateVersionedEntity extends VersionedEntity {

    @NotNull
    DateTime getCreationDate();

    @NotNull
    DateTime getModificationDate();

    @NotNull
    DateVersionedEntity clone();
}
