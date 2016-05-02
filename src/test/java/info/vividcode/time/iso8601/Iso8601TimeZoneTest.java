package info.vividcode.time.iso8601;

import org.junit.Test;

import static info.vividcode.time.iso8601.Matchers.*;
import static org.junit.Assert.assertThat;

public class Iso8601TimeZoneTest {

    @Test
    public void utc() {
        Iso8601TimeZone timeZone = Iso8601TimeZone.utc();
        assertThat(timeZone, hasStringRepresentation("Z"));
        assertThat(timeZone, hasOffsetInMinutes(0));
    }

    @Test
    public void create_withOnlyHours() {
        {
            Iso8601TimeZone timeZone = Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 1);
            assertThat(timeZone, hasStringRepresentation("+01"));
            assertThat(timeZone, hasOffsetInMinutes(+60));
        }
        {
            Iso8601TimeZone timeZone = Iso8601TimeZone.create(Iso8601TimeZone.Sign.HYPHEN_MINUS, 1);
            assertThat(timeZone, hasStringRepresentation("-01"));
            assertThat(timeZone, hasOffsetInMinutes(-60));
        }
        {
            Iso8601TimeZone timeZone = Iso8601TimeZone.create(Iso8601TimeZone.Sign.NORMAL_MINUS, 1);
            assertThat(timeZone, hasStringRepresentation("\u221201")); // U+2212 is a minus sign.
            assertThat(timeZone, hasOffsetInMinutes(-60));
        }
    }

    @Test
    public void create_withHoursAndMinutes() {
        {
            Iso8601TimeZone timeZone = Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 1, 30, false);
            assertThat(timeZone, hasStringRepresentation("+0130"));
            assertThat(timeZone, hasOffsetInMinutes(+90));
        }
        {
            Iso8601TimeZone timeZone = Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 1, 30, true);
            assertThat(timeZone, hasStringRepresentation("+01:30"));
            assertThat(timeZone, hasOffsetInMinutes(+90));
        }
        {
            Iso8601TimeZone timeZone = Iso8601TimeZone.create(Iso8601TimeZone.Sign.HYPHEN_MINUS, 1, 30, false);
            assertThat(timeZone, hasStringRepresentation("-0130"));
            assertThat(timeZone, hasOffsetInMinutes(-90));
        }
        {
            Iso8601TimeZone timeZone = Iso8601TimeZone.create(Iso8601TimeZone.Sign.HYPHEN_MINUS, 1, 30, true);
            assertThat(timeZone, hasStringRepresentation("-01:30"));
            assertThat(timeZone, hasOffsetInMinutes(-90));
        }
        {
            Iso8601TimeZone timeZone = Iso8601TimeZone.create(Iso8601TimeZone.Sign.NORMAL_MINUS, 1, 30, false);
            assertThat(timeZone, hasStringRepresentation("\u22120130")); // U+2212 is a minus sign.
            assertThat(timeZone, hasOffsetInMinutes(-90));
        }
        {
            Iso8601TimeZone timeZone = Iso8601TimeZone.create(Iso8601TimeZone.Sign.NORMAL_MINUS, 1, 30, true);
            assertThat(timeZone, hasStringRepresentation("\u221201:30")); // U+2212 is a minus sign.
            assertThat(timeZone, hasOffsetInMinutes(-90));
        }
    }

    @Test
    public void equality() {
        {
            Iso8601TimeZone timeZone1 = Iso8601TimeZone.utc();
            Iso8601TimeZone timeZone2 = Iso8601TimeZone.utc();
            assertThat(twoObjects(timeZone1, timeZone2), areEqual());
        }
        {
            Iso8601TimeZone timeZone1 = Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 1);
            Iso8601TimeZone timeZone2 = Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 1);
            assertThat(twoObjects(timeZone1, timeZone2), areEqual());
        }
        {
            Iso8601TimeZone timeZone1 = Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 1);
            Iso8601TimeZone timeZone2 = Iso8601TimeZone.create(Iso8601TimeZone.Sign.PLUS, 2);
            assertThat("Offset times are not equal.",
                    twoObjects(timeZone1, timeZone2), areNotEqual());
        }
        {
            Iso8601TimeZone timeZone1 = Iso8601TimeZone.create(Iso8601TimeZone.Sign.HYPHEN_MINUS, 1);
            Iso8601TimeZone timeZone2 = Iso8601TimeZone.create(Iso8601TimeZone.Sign.NORMAL_MINUS, 1);
            assertThat("Signs are not equal.",
                    twoObjects(timeZone1, timeZone2), areNotEqual());
        }
    }

}
