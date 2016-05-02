package info.vividcode.time.iso8601;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Iso8601ExtendedOffsetDateTimeFormatTest {

    @Test
    public void construction_usual() {
        new Iso8601ExtendedOffsetDateTimeFormat();
        new Iso8601ExtendedOffsetDateTimeFormat(Iso8601TimeZone.utc());
        new Iso8601ExtendedOffsetDateTimeFormat(Iso8601TimeZone.create(Iso8601TimeZone.Sign.HYPHEN_MINUS, 9, 0, false));
    }

    @Test
    public void format_date() {
        DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat();
        long time = (365 + 31 + 3 - 1) * 24 * 60 * 60 * 1000L;
        Date d = new Date(time);
        assertThat(f.format(d), equalTo("1971-02-03T00:00:00Z"));
    }

    @Test
    public void format_time() {
        DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat();
        long time = (((60 + 2) * 60) + 3) * 1000L;
        Date d = new Date(time);
        assertThat(f.format(d), equalTo("1970-01-01T01:02:03Z"));
    }

    @Test
    public void format_timezone() {
        Date d = new Date(0);
        { // No offset.
            {
                DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat();
                assertThat(f.format(d), equalTo("1970-01-01T00:00:00Z"));
            }
            {
                DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat(Iso8601TimeZone.utc());
                assertThat(f.format(d), equalTo("1970-01-01T00:00:00Z"));
            }
            {
                Iso8601TimeZone tz = Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 0);
                DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat(tz);
                assertThat(f.format(d), equalTo("1970-01-01T00:00:00+00"));
            }
            {
                Iso8601TimeZone tz = Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 0, 0, false);
                DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat(tz);
                assertThat(f.format(d), equalTo("1970-01-01T00:00:00+0000"));
            }
            {
                Iso8601TimeZone tz = Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 0, 0, true);
                DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat(tz);
                assertThat(f.format(d), equalTo("1970-01-01T00:00:00+00:00"));
            }
        }
        { // With offset
            {
                Iso8601TimeZone tz = Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 9, 0, false);
                DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat(tz);
                assertThat(f.format(d), equalTo("1970-01-01T09:00:00+0900"));
            }
            {
                Iso8601TimeZone tz = Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 9, 30, false);
                DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat(tz);
                assertThat(f.format(d), equalTo("1970-01-01T09:30:00+0930"));
            }
            {
                Iso8601TimeZone tz = Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 9, 30, true);
                DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat(tz);
                assertThat(f.format(d), equalTo("1970-01-01T09:30:00+09:30"));
            }
            {
                Iso8601TimeZone tz = Iso8601TimeZone.create(Iso8601TimeZone.Sign.NORMAL_MINUS, 9, 30, true);
                DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat(tz);
                assertThat(f.format(d), equalTo("1969-12-31T14:30:00\u221209:30"));
            }
            {
                Iso8601TimeZone tz = Iso8601TimeZone.create(Iso8601TimeZone.Sign.HYPHEN_MINUS, 9, 30, true);
                DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat(tz);
                assertThat(f.format(d), equalTo("1969-12-31T14:30:00-09:30"));
            }
        }
    }

    @Test
    public void parse_date() throws ParseException {
        DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat();
        long refTime = (365 + 31 + 3 - 1) * 24 * 60 * 60 * 1000L;
        Date d = f.parse("1971-02-03T00:00:00Z");
        assertThat(d.getTime(), equalTo(refTime));
    }

    @Test
    public void parse_time() throws ParseException {
        DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat();
        long refTime = (((60 + 2) * 60) + 3) * 1000L;
        Date d = f.parse("1970-01-01T01:02:03Z");
        assertThat(d.getTime(), equalTo(refTime));
    }

    @Test
    public void parse_timezone() throws ParseException {
        DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat();
        long refTime = 0L;
        { // No offset.
            {
                Date d = f.parse("1970-01-01T00:00:00Z");
                assertThat(d.getTime(), equalTo(refTime));
            }
            {
                Date d = f.parse("1970-01-01T00:00:00+00");
                assertThat(d.getTime(), equalTo(refTime));
            }
        }
        { // With offset
            {
                Date d = f.parse("1970-01-01T09:00:00+09:00");
                assertThat(d.getTime(), equalTo(refTime));
            }
            {
                Date d = f.parse("1970-01-01T09:00:00+0900");
                assertThat(d.getTime(), equalTo(refTime));
            }
            {
                Date d = f.parse("1970-01-01T09:00:00+09");
                assertThat(d.getTime(), equalTo(refTime));
            }
            {
                Date d = f.parse("1969-12-31T14:30:00\u221209:30");
                assertThat(d.getTime(), equalTo(refTime));
            }
            {
                Date d = f.parse("1969-12-31T14:30:00-09:30");
                assertThat(d.getTime(), equalTo(refTime));
            }
        }
    }

    private static Exception tryParse(DateFormat f, String target) {
        try {
            f.parse(target);
        } catch (Exception e) {
            return e;
        }
        return null;
    }

    @Test
    public void parse_withInvalidParameter() throws ParseException {
        DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat();
        assertThat(tryParse(f, "1988"), is(instanceOf(ParseException.class)));
        assertThat(tryParse(f, "1988-01-01"), is(instanceOf(ParseException.class)));
        assertThat(tryParse(f, "1988-01-01T00:00:00"), is(instanceOf(ParseException.class)));
        assertThat(tryParse(f, "1988-01-01T00:00:00d"), is(instanceOf(ParseException.class)));
        assertThat(tryParse(f, "1988-01-01T00:00:00+"), is(instanceOf(ParseException.class)));
        assertThat(tryParse(f, "1988-01-01T00:00:00+1d"), is(instanceOf(ParseException.class)));
    }

}
