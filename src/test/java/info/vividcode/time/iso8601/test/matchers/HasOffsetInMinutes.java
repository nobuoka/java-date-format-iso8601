package info.vividcode.time.iso8601.test.matchers;

import info.vividcode.time.iso8601.Iso8601TimeZone;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.TimeUnit;

public class HasOffsetInMinutes extends TypeSafeMatcher<Iso8601TimeZone> {

    private final int mOffsetInMinutes;

    private HasOffsetInMinutes(int offsetInMinutes) {
        mOffsetInMinutes = offsetInMinutes;
    }

    @Override
    protected boolean matchesSafely(Iso8601TimeZone timeZone) {
        return mOffsetInMinutes == timeZone.getOffsetTime(TimeUnit.MINUTES);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("has offset: ").appendValue(mOffsetInMinutes).appendText(" minutes");
    }

    @Override
    protected void describeMismatchSafely(Iso8601TimeZone item, Description mismatchDescription) {
        mismatchDescription.appendText("has offset: ")
                .appendValue(item.getOffsetTime(TimeUnit.MINUTES)).appendText(" minutes");
    }

    @Factory
    public static Matcher<Iso8601TimeZone> hasOffsetInMinutes(int offsetInMinutes) {
        return new HasOffsetInMinutes(offsetInMinutes);
    }

}
