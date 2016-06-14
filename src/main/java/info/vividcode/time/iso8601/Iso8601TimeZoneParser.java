package info.vividcode.time.iso8601;

import java.text.ParsePosition;

public final class Iso8601TimeZoneParser {

    public static final Iso8601TimeZoneParser INSTANCE = new Iso8601TimeZoneParser();

    public static Iso8601TimeZone parse(String source) throws IllegalArgumentException {
        ParsePosition pos = new ParsePosition(0);
        Iso8601TimeZone timeZone = INSTANCE.parseTimeZone(source, pos);
        if (0 <= pos.getErrorIndex()) {
            throw new IllegalArgumentException(
                    "Parse error (source : " + source + ", error at : " + pos.getErrorIndex() + ")");
        } else if (pos.getIndex() < source.length()) {
            throw new IllegalArgumentException(
                    "Source string contains useless substring (source : " + source + ")");
        }
        return timeZone;
    }

    public Iso8601TimeZone parseTimeZone(String source, ParsePosition pos) {
        int firstIndex = pos.getIndex();
        try {
            Character zOrNull = ParseUtil.parseCharOrNull(source, 'Z', pos);
            if (zOrNull != null) {
                return Iso8601TimeZone.utc();
            }

            Iso8601TimeZone.Sign sign = parseSign(source, pos);
            int timeZoneHh = ParseUtil.parseNumericalChars(source, 2, pos);
            Integer timeZoneMm = null;
            boolean includeColon = (ParseUtil.parseCharOrNull(source, ':', pos) != null);
            try {
                timeZoneMm = ParseUtil.parseNumericalChars(source, 2, pos);
            } catch (ParseException e) {
                if (includeColon) throw e;
            }

            return  (timeZoneMm != null ?
                    Iso8601TimeZone.create(sign, timeZoneHh, timeZoneMm, includeColon) :
                    Iso8601TimeZone.create(sign, timeZoneHh));
        } catch (ParseException e) {
            pos.setIndex(firstIndex);
            pos.setErrorIndex(e.errorIndex);
            return null;
        }
    }

    private static Iso8601TimeZone.Sign parseSign(String source, ParsePosition pos) throws ParseException {
        int firstPosition = pos.getIndex();
        for (Iso8601TimeZone.Sign sign : Iso8601TimeZone.Sign.values()) {
            Character signOrNull = ParseUtil.parseCharOrNull(source, sign.getSignChar(), pos);
            if (signOrNull != null) return sign;
        }
        throw new ParseException(firstPosition);
    }

}
