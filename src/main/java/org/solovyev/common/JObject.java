package org.solovyev.common;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 6/9/12
 * Time: 8:50 PM
 */
public abstract class JObject implements Cloneable {

    @NotNull
    @Override
    public JObject clone() {
        try {
            return (JObject) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
