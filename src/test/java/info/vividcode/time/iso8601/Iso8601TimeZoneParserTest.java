package info.vividcode.time.iso8601;

import org.junit.Test;

import java.text.ParsePosition;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class Iso8601TimeZoneParserTest {

    private static Exception parseError(String timezoneStr) {
        Exception exception = null;
        try {
            Iso8601TimeZoneParser.parse(timezoneStr);
        } catch (Exception e) {
            exception = e;
        }
        return exception;
    }

    @Test
    public void parse_usual() {
        {
            Iso8601TimeZone parsed = Iso8601TimeZoneParser.parse("Z");
            assertThat(parsed, equalTo(Iso8601TimeZone.utc()));
        }
        {
            Iso8601TimeZone parsed = Iso8601TimeZoneParser.parse("+00:00");
            assertThat(parsed, equalTo(Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 0, 0, true)));
        }
        {
            Iso8601TimeZone parsed = Iso8601TimeZoneParser.parse("+0000");
            assertThat(parsed, equalTo(Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 0, 0, false)));
        }
        {
            Iso8601TimeZone parsed = Iso8601TimeZoneParser.parse("+00");
            assertThat(parsed, equalTo(Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 0)));
        }
        {
            Iso8601TimeZone parsed = Iso8601TimeZoneParser.parse("-05");
            assertThat(parsed, equalTo(Iso8601TimeZone.create(Iso8601TimeZone.Sign.HYPHEN_MINUS, 5)));
        }
        {
            Iso8601TimeZone parsed = Iso8601TimeZoneParser.parse("\u221209:30"); // U+2212 is a minus sign.
            assertThat(parsed, equalTo(Iso8601TimeZone.create(Iso8601TimeZone.Sign.NORMAL_MINUS, 9, 30, true)));
        }
    }

    @Test
    public void parse_withIllegalArgument() {
        assertThat("“ZZ” is not applicable.",
                parseError("ZZ"), instanceOf(IllegalArgumentException.class));
        assertThat("Needs sign character.",
                parseError("09"), instanceOf(IllegalArgumentException.class));
        assertThat("Needs two digit characters.",
                parseError("+0"), instanceOf(IllegalArgumentException.class));
        assertThat("Needs two or four digit characters.",
                parseError("+090"), instanceOf(IllegalArgumentException.class));
        assertThat("Needs colon instead of minus sign.",
                parseError("+09-00"), instanceOf(IllegalArgumentException.class));
        assertThat("Too long.",
                parseError("+09000"), instanceOf(IllegalArgumentException.class));
    }

    @Test
    public void parseTimeZone_usual() {
        Iso8601TimeZoneParser parser = Iso8601TimeZoneParser.INSTANCE;

        {
            ParsePosition pos = new ParsePosition(0);
            Iso8601TimeZone timeZone = parser.parseTimeZone("Z", pos);
            assertThat(timeZone, equalTo(Iso8601TimeZone.utc()));
            assertThat(pos.getIndex(), equalTo(1));
            assertThat(pos.getErrorIndex(), equalTo(-1));
        }
        {
            ParsePosition pos = new ParsePosition(8);
            Iso8601TimeZone timeZone = parser.parseTimeZone("not used+0000not used", pos);
            assertThat(timeZone, equalTo(Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 0, 0, false)));
            assertThat(pos.getIndex(), equalTo(13));
            assertThat(pos.getErrorIndex(), equalTo(-1));
        }
    }

    @Test
    public void parseTimeZone_withParseError() {
        Iso8601TimeZoneParser parser = Iso8601TimeZoneParser.INSTANCE;

        {
            ParsePosition pos = new ParsePosition(0);
            Iso8601TimeZone timeZone = parser.parseTimeZone("d", pos);
            assertThat(timeZone, is(nullValue()));
            assertThat("In case that the parse failed, the start position is returned",
                    pos.getIndex(), is(equalTo(0)));
            assertThat(pos.getErrorIndex(), is(equalTo(0)));
        }
        {
            ParsePosition pos = new ParsePosition(8);
            Iso8601TimeZone timeZone = parser.parseTimeZone("not used+0d00not used", pos);
            assertThat(timeZone, is(nullValue()));
            assertThat("In case that the parse failed, the start position is returned",
                    pos.getIndex(), is(equalTo(8)));
            assertThat(pos.getErrorIndex(), is(equalTo(10)));
        }
    }

}
