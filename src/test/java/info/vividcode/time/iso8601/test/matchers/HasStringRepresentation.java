package info.vividcode.time.iso8601.test.matchers;

import info.vividcode.time.iso8601.Iso8601TimeZone;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class HasStringRepresentation extends TypeSafeMatcher<Iso8601TimeZone> {

    private final String mStringRepresentation;

    private HasStringRepresentation(String stringRepresentation) {
        mStringRepresentation = stringRepresentation;
    }

    @Override
    protected boolean matchesSafely(Iso8601TimeZone localDate) {
        return mStringRepresentation.equals(localDate.getStringRepresentation());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("has string representation: ").appendValue(mStringRepresentation);
    }

    @Override
    protected void describeMismatchSafely(Iso8601TimeZone item, Description mismatchDescription) {
        mismatchDescription.appendText("has string representation: ").appendValue(item.getStringRepresentation());
    }

    @Factory
    public static Matcher<Iso8601TimeZone> hasStringRepresentation(String stringRepresentation) {
        return new HasStringRepresentation(stringRepresentation);
    }

}
