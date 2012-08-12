package org.solovyev.android.samples.prefs;

import org.solovyev.android.prefs.NumberIntervalPreference;
import org.solovyev.android.prefs.NumberToStringPreference;
import org.solovyev.android.prefs.Preference;
import org.solovyev.android.prefs.StringPreference;
import org.solovyev.common.interval.Interval;
import org.solovyev.common.interval.IntervalImpl;

/**
 * User: serso
 * Date: 8/8/12
 * Time: 3:01 AM
 */
public final class Preferences {
    public static final Preference<Choice> toBeOrNotTobe = StringPreference.newInstance("to_be_or_not_to_be", Choice.to_be, Choice.class);
    public static final Preference<Integer> integerNumber = new NumberToStringPreference<Integer>("integer_number", 5, Integer.class);
    public static final Preference<Double> doubleNumber = new NumberToStringPreference<Double>("double_number", 0.5d, Double.class);
    public static final Preference<Country> country = StringPreference.newInstance("country", Country.russia, Country.class);
    public static final Preference<Interval<Float>> floatInterval = new NumberIntervalPreference<Float>("float_interval", IntervalImpl.newClosed(35f, 350f), Float.class);
    public static final Preference<Interval<Integer>> integerInterval = new NumberIntervalPreference<Integer>("integer_interval", IntervalImpl.newClosed(35, 350), Integer.class);
}
