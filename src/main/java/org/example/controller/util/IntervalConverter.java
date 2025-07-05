package org.example.controller.util;

import org.example.dto.IntervalRequest;
import org.example.entity.Interval;
import org.example.exception.DatetimeParseException;
import org.example.exception.InvalidTimeIntervalException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IntervalConverter {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    public static Interval convert(IntervalRequest request)
            throws InvalidTimeIntervalException, DatetimeParseException {

        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

        String date = request.getDate();
        String startTimeStr = request.getStartTime();
        String endTimeStr = request.getEndTime();

        Date start;
        Date end;
        try {
            start = dateFormat.parse(date + " " + startTimeStr);
            end = dateFormat.parse(date + " " + endTimeStr);
        } catch (ParseException e) {
            throw new DatetimeParseException("Invalid date/time format.");
        }

        return new Interval.IntervalBuilder()
                .startTime(start)
                .endTime(end)
                .build();
    }
}
