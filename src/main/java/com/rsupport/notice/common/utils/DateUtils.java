package com.rsupport.notice.common.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    public static long getSecondsUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.truncatedTo(ChronoUnit.DAYS).plusDays(1);
        return ChronoUnit.SECONDS.between(now, midnight);
    }
}
