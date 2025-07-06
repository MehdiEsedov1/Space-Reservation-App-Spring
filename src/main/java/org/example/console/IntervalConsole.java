package org.example.console;

import org.example.entity.Interval;
import org.example.entity.Interval.IntervalBuilder;
import org.example.exception.DatetimeParseException;
import org.example.exception.InvalidTimeIntervalException;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.example.console.util.ConsoleReader.readLine;

@Component
public class IntervalConsole {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    public Interval getInterval() throws InvalidTimeIntervalException, DatetimeParseException {
        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

        String dateInput = readLine("Enter reservation date (e.g., 2025-07-06): ");
        String startInput = readLine("Enter start time (e.g., 09:00): ");
        String endInput = readLine("Enter end time (e.g., 11:30): ");

        Date startTime = parseDateTime(dateFormat, dateInput, startInput);
        Date endTime = parseDateTime(dateFormat, dateInput, endInput);

        return new IntervalBuilder()
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    private Date parseDateTime(DateFormat formatter, String date, String time) throws DatetimeParseException {
        try {
            return formatter.parse(date + " " + time);
        } catch (ParseException e) {
            throw new DatetimeParseException(
                    "Invalid format. Please make sure your date is in yyyy-MM-dd format and time is in HH:mm format (e.g., 2025-07-06 14:00)."
            );
        }
    }
}
