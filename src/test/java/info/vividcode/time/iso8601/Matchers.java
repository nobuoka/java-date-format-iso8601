package info.vividcode.time.iso8601;

import info.vividcode.time.iso8601.test.TwoObjects;
import info.vividcode.time.iso8601.test.matchers.AreEqual;
import info.vividcode.time.iso8601.test.matchers.AreNotEqual;
import info.vividcode.time.iso8601.test.matchers.HasOffsetInMinutes;
import info.vividcode.time.iso8601.test.matchers.HasStringRepresentation;
import org.hamcrest.Matcher;

class Matchers {

    static Matcher<Iso8601TimeZone> hasStringRepresentation(String stringRepresentation) {
        return HasStringRepresentation.hasStringRepresentation(stringRepresentation);
    }

    static Matcher<Iso8601TimeZone> hasOffsetInMinutes(int offsetInMinutes) {
        return HasOffsetInMinutes.hasOffsetInMinutes(offsetInMinutes);
    }

    static <T> TwoObjects<T> twoObjects(T obj1, T obj2) {
        return new TwoObjects<T>(obj1, obj2);
    }

    static Matcher<TwoObjects<?>> areEqual() {
        return AreEqual.areEqual();
    }

    static Matcher<TwoObjects<?>> areNotEqual() {
        return AreNotEqual.areNotEqual();
    }

}
