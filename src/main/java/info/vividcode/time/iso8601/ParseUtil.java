package info.vividcode.time.iso8601;

import java.text.ParsePosition;

class ParseUtil {

    static Character parseCharOrNull(String source, char expectedChar, ParsePosition pos) {
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

    static int parseNumericalChars(String source, int count, ParsePosition pos) throws ParseException {
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

}
