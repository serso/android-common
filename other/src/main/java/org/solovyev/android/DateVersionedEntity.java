package org.solovyev.android;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.solovyev.common.VersionedEntity;

/**
 * User: serso
 * Date: 4/29/12
 * Time: 9:52 PM
 */
public interface DateVersionedEntity<I> extends VersionedEntity<I> {

    @NotNull
    DateTime getCreationDate();

    @NotNull
    DateTime getModificationDate();

    @NotNull
    DateVersionedEntity<I> clone();
}
