ISO 8601 DateFormat
==============================

This library provides a subclass of [`DateFormat` class](https://docs.oracle.com/javase/8/docs/api/java/text/DateFormat.html)
to parse and format dates formats of which are specified by [ISO 8601](http://www.iso.org/iso/home/standards/iso8601.htm)
(or by [RFC 3339](https://tools.ietf.org/html/rfc3339) or [W3C-DTF](https://www.w3.org/TR/1998/NOTE-datetime-19980827)).

Currently, only combined date and time representations with extended format are supported.

* 2016-01-01T00:30:21Z
* 2016-01-01T09:30:21+09:00

## Targets of this library

* Java SE 6 environments
* Android platforms

On these runtime environments, the `SimpleDateFormat` class doesn't support ISO 8601 time zone fully.
(On Android platforms, the `Z` pattern character is able to read a time zone style of which is `±HH:MM`. But it cannot read `Z`.)

On Java SE 7 environments (or later), you can use `X` pattern character with `SimpleDateFormat` class.
(e.g. `new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.ROOT)`.)

On Java SE 8 environments (or later), you should probably use the
[Date and Time API](http://www.oracle.com/technetwork/articles/java/jf14-date-time-2125367.html).
If you want to use Date and Time API on Android platforms, see [ThreeTen Android Backport](https://github.com/JakeWharton/ThreeTenABP).

## Usage

This library is published to JCenter.

```java
import info.vividcode.time.iso8601.Iso8601ExtendedOffsetDateTimeFormat;

DateFormat f = new Iso8601ExtendedOffsetDateTimeFormat();
Date d1 = f.parse("1970-01-01T00:00:00Z");
Date d2 = f.parse("1970-01-01T09:00:00+09:00");
```

## License

```
Copyright 2016 NOBUOKA Yu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
