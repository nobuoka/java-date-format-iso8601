package info.vividcode.time.iso8601;

import java.text.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * This is a class for formatting and parsing dates which have combined date and time representations
 * with extended format described in ISO 8601.
 * <ul>
 *     <li>2016-01-01T00:30:21Z</li>
 *     <li>2016-01-01T09:30:21+09:00</li>
 * </ul>
 * <p>
 * This class targets mainly Java SE 6 environments and Android platforms.
 * On Java SE 7 environments, you can use the date and time pattern letter {@code X} to represent a ISO 8601 time zone
 * with {@link SimpleDateFormat}.
 * (e.g. {@code new SImpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.ROOT)}.)
 * On Java SE 8 environments, you should probably use the <a href="http://www.oracle.com/technetwork/articles/java/jf14-date-time-2125367.html"
 * >Date and Time API</a>.
 */
public class Iso8601ExtendedOffsetDateTimeFormat extends DateFormat {

    private final SimpleDateFormat mDateTimeSimpleFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT);
    {
        mDateTimeSimpleFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
    }

    private final Iso8601TimeZoneParser mTimeZoneParser = Iso8601TimeZoneParser.INSTANCE;

    private final Iso8601TimeZone mTimeZoneOnFormat;

    public Iso8601ExtendedOffsetDateTimeFormat(Iso8601TimeZone timezoneOnFormat) {
        if (timezoneOnFormat == null)
            throw new IllegalArgumentException("`timezoneOnFormat` must not be null.");

        super.numberFormat = NumberFormat.getIntegerInstance(Locale.ROOT);
        super.calendar = new GregorianCalendar();
        mTimeZoneOnFormat = timezoneOnFormat;
    }

    public Iso8601ExtendedOffsetDateTimeFormat() {
        this(Iso8601TimeZone.utc());
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        long offsetTimeInMs = mTimeZoneOnFormat.getOffsetTime(TimeUnit.MILLISECONDS);
        Date localDate = (offsetTimeInMs != 0 ? new Date(date.getTime() + offsetTimeInMs) : date);
        mDateTimeSimpleFormat.format(localDate, toAppendTo, fieldPosition);
        toAppendTo.append(mTimeZoneOnFormat.getStringRepresentation());
        return toAppendTo;
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        int firstIndex = pos.getIndex();

        NumberFormat f = super.numberFormat;
        super.calendar.setTimeZone(TimeZone.getTimeZone("GMT"));

        int year = f.parse(source, pos).intValue();
        ParseUtil.parseCharOrNull(source, '-', pos);
        int month = f.parse(source, pos).intValue();
        ParseUtil.parseCharOrNull(source, '-', pos);
        int dayOfMonth = f.parse(source, pos).intValue();

        ParseUtil.parseCharOrNull(source, 'T', pos);

        int hour = f.parse(source, pos).intValue();
        ParseUtil.parseCharOrNull(source, ':', pos);
        int minute = f.parse(source, pos).intValue();
        ParseUtil.parseCharOrNull(source, ':', pos);
        int second = f.parse(source, pos).intValue();

        super.calendar.set(year, month - 1, dayOfMonth, hour, minute, second);
        super.calendar.clear(Calendar.MILLISECOND);

        Date date = super.calendar.getTime();

        Iso8601TimeZone timeZone = mTimeZoneParser.parseTimeZone(source, pos);
        if (timeZone == null) {
            pos.setIndex(firstIndex);
            return null;
        }

        date.setTime(date.getTime() - timeZone.getOffsetTime(TimeUnit.MILLISECONDS));
        return date;
    }

}
