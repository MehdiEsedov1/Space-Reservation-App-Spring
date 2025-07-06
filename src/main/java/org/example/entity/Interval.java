package org.example.entity;

import jakarta.persistence.Embeddable;
import org.example.exception.InvalidTimeIntervalException;

import java.io.Serializable;
import java.util.Date;

@Embeddable
public class Interval implements Serializable {

    private Date startTime;
    private Date endTime;

    protected Interval() {
    }

    private Interval(IntervalBuilder builder) {
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
    }

    public static boolean isOverlap(Interval a, Interval b) {
        return !(a.endTime.before(b.startTime) || b.endTime.before(a.startTime));
    }

    public Date getStartTime() {
        return new Date(startTime.getTime());
    }

    public Date getEndTime() {
        return new Date(endTime.getTime());
    }

    public static final class IntervalBuilder {

        private Date startTime;
        private Date endTime;

        public IntervalBuilder startTime(Date startTime) {
            this.startTime = startTime != null ? new Date(startTime.getTime()) : null;
            return this;
        }

        public IntervalBuilder endTime(Date endTime) {
            this.endTime = endTime != null ? new Date(endTime.getTime()) : null;
            return this;
        }

        public Interval build() throws InvalidTimeIntervalException {
            if (startTime == null || endTime == null) {
                throw new InvalidTimeIntervalException("Start time and end time must not be null.");
            }
            if (startTime.after(endTime)) {
                throw new InvalidTimeIntervalException("Start time must be before the end time!");
            }
            return new Interval(this);
        }
    }
}
