package info.vividcode.time.iso8601;

class ParseException extends Exception {

    final int errorIndex;

    ParseException(int errorIndex) {
        this.errorIndex = errorIndex;
    }

}
