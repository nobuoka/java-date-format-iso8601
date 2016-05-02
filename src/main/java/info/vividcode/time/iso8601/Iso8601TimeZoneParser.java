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
            Character zOrNull = parseCharOrNull(source, 'Z', pos);
            if (zOrNull != null) {
                return Iso8601TimeZone.utc();
            }

            Iso8601TimeZone.Sign sign = parseSign(source, pos);
            int timeZoneHh = parseNumericalChars(source, 2, pos);
            Integer timeZoneMm = null;
            boolean includeColon = (parseCharOrNull(source, ':', pos) != null);
            try {
                timeZoneMm = parseNumericalChars(source, 2, pos);
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
            Character signOrNull = parseCharOrNull(source, sign.getSignChar(), pos);
            if (signOrNull != null) return sign;
        }
        throw new ParseException(firstPosition);
    }

    private static Character parseCharOrNull(String source, char expectedChar, ParsePosition pos) throws ParseException {
        int firstPosition = pos.getIndex();
        if (source.length() <= firstPosition) return null;

        char c = source.charAt(firstPosition);
        if (expectedChar == c) {
            pos.setIndex(firstPosition + 1);
            return c;
        } else {
            return null;
        }
    }

    private static int parseNumericalChars(String source, int count, ParsePosition pos) throws ParseException {
        int firstIndex = pos.getIndex();
        int numVal = 0;
        int endIndex = firstIndex + count;
        for (int i = firstIndex; i < endIndex; i++) {
            if (source.length() <= i) throw new ParseException(i);
            char c = source.charAt(i);
            if ('0' <= c && c <= '9') {
                numVal *= 10;
                numVal += c - '0';
            } else {
                throw new ParseException(i);
            }
        }
        pos.setIndex(firstIndex + count);
        return numVal;
    }

    private static class ParseException extends Exception {
        final int errorIndex;

        ParseException(int errorIndex) {
            this.errorIndex = errorIndex;
        }
    }

}
