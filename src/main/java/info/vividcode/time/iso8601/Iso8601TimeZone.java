package info.vividcode.time.iso8601;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class Iso8601TimeZone {

    private static final Iso8601TimeZone UTC = new Iso8601TimeZone("Z", 0);

    private final String mStringRepresentation;

    private final long mOffsetTimeInMinutes;

    private Iso8601TimeZone(String stringRepresentation, long offsetTimeInMinutes) {
        mStringRepresentation = stringRepresentation;
        mOffsetTimeInMinutes = offsetTimeInMinutes;
    }

    public static Iso8601TimeZone utc() {
        return UTC;
    }

    public static Iso8601TimeZone create(Sign sign, int hours) {
        return builder(sign, hours).build();
    }

    public static Iso8601TimeZone create(Sign sign, int hours, int minus, boolean includeColon) {
        BuilderMinutesOrColonOrBuild b = builder(sign, hours);
        return (includeColon ? b.colon() : b).minutes(minus).build();
    }

    private static Iso8601TimeZone.BuilderMinutesOrColonOrBuild builder(Sign sign, int hours) {
        return builder().sign(sign).hours(hours);
    }

    public long getOffsetTime(TimeUnit unit) {
        return unit.convert(mOffsetTimeInMinutes, TimeUnit.MINUTES);
    }

    public String getStringRepresentation() {
        return mStringRepresentation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Iso8601TimeZone)) return false;
        Iso8601TimeZone that = (Iso8601TimeZone) obj;

        if (mOffsetTimeInMinutes != that.mOffsetTimeInMinutes) return false;
        if (!mStringRepresentation.equals(that.mStringRepresentation)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + ((Long) mOffsetTimeInMinutes).hashCode();
        hash = hash * 31 + mStringRepresentation.hashCode();
        return hash;
    }

    public enum Sign {
        PLUS('+', +1),
        NORMAL_MINUS('−', -1),
        HYPHEN_MINUS('-', -1),
        ;

        private final char mSignChar;
        private final int mSignUnit;
        Sign(char signChar, int unit) {
            mSignChar = signChar;
            mSignUnit = unit;
        }
        public char getSignChar() {
            return mSignChar;
        }
        private int getUnit() {
            return mSignUnit;
        }
    }

    private static BuilderSign builder() {
        return new Builder();
    }

    private interface BuilderSign {
        BuilderHours sign(Sign sign);
    }

    private interface BuilderHours {
        BuilderMinutesOrColonOrBuild hours(int val);
    }

    private interface BuilderMinutes {
        BuilderBuild minutes(int val);
    }

    private interface BuilderMinutesOrColonOrBuild extends BuilderBuild, BuilderMinutes {
        BuilderMinutes colon();
    }

    private interface BuilderBuild {
        Iso8601TimeZone build();
    }

    private static class Builder implements BuilderBuild, BuilderSign, BuilderHours, BuilderMinutesOrColonOrBuild {
        private StringBuilder mStringRepresentationBuilder = new StringBuilder();
        private int mOffsetTimeInMinutes = 0;
        private int mSignUnit = 0;

        @Override
        public BuilderHours sign(Sign val) {
            mStringRepresentationBuilder.append(val.getSignChar());
            mSignUnit = val.getUnit();
            return this;
        }

        @Override
        public BuilderMinutesOrColonOrBuild hours(int val) {
            mStringRepresentationBuilder.append(String.format(Locale.ROOT, "%02d", val));
            mOffsetTimeInMinutes += mSignUnit * val * 60;
            return this;
        }

        @Override
        public BuilderMinutes colon() {
            mStringRepresentationBuilder.append(':');
            return this;
        }

        @Override
        public BuilderBuild minutes(int val) {
            mStringRepresentationBuilder.append(String.format(Locale.ROOT, "%02d", val));
            mOffsetTimeInMinutes += mSignUnit * val;
            return this;
        }

        @Override
        public Iso8601TimeZone build() {
            return new Iso8601TimeZone(mStringRepresentationBuilder.toString(), mOffsetTimeInMinutes);
        }
    }

}
