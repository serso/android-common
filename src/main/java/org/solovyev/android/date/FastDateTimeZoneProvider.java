package org.solovyev.android.date;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.Provider;

import java.util.*;

/**
 * User: serso
 * Date: 4/29/12
 * Time: 9:36 PM
 */
public class FastDateTimeZoneProvider implements Provider {

    @NotNull
    public static final Set<String> availableIds = new HashSet<String>();

    static {
        availableIds.addAll(Arrays.asList(TimeZone.getAvailableIDs()));
    }

    @NotNull
    public DateTimeZone getZone(@Nullable String id) {
        if (id == null) {
            return DateTimeZone.UTC;
        }

        final TimeZone tz = TimeZone.getTimeZone(id);
        if (tz == null) {
            return DateTimeZone.UTC;
        }

        int rawOffset = tz.getRawOffset();

        // sub-optimal. could be improved to only create a new Date every few minutes
        if (tz.inDaylightTime(new Date())) {
            rawOffset += tz.getDSTSavings();
        }

        return DateTimeZone.forOffsetMillis(rawOffset);
    }

    @NotNull
    public Set<String> getAvailableIDs() {
        return availableIds;
    }
}

