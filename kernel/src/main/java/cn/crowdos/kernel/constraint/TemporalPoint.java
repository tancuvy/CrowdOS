package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TemporalPoint implements Constraint{
    private final long shift;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public TemporalPoint(String timePoint, long shift) {
        LocalTime point = LocalTime.parse(timePoint);
        this.shift = shift;
        this.startTime = point.plusMinutes(shift);
        this.endTime = point.plusMinutes(shift);
    }
    private TemporalPoint(LocalTime startTime, LocalTime endTime, long shift) throws InvalidConstraintException {
        this.shift = shift;
        this.startTime = startTime;
        this.endTime = endTime;
        if (startTime.isAfter(endTime)){
            throw new InvalidConstraintException(String.format("startTime %s is after endTime %s", startTime, endTime));
        }
    }

    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                try {
                    return Collections.singletonList(new TemporalPoint(startTime, endTime, shift));
                } catch (InvalidConstraintException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public List<Constraint> scaleDecompose(int scale) throws DecomposeException {
                if (scale < 0) throw new DecomposeException("invalid decompose scale");

                if (scale == 1) return trivialDecompose();
                Duration duration = Duration.between(startTime, endTime);
                long totalSeconds = duration.getSeconds();
                long secondsPerInterval = totalSeconds / scale;
                List<Constraint> subConstraints = new ArrayList<>(scale);
                for (int i = 0; i < scale; i++) {
                    LocalTime intervalStartTime = startTime.plusSeconds(i * secondsPerInterval);
                    LocalTime intervalEndTime = intervalStartTime.plusSeconds(secondsPerInterval);
                    try {
                        subConstraints.add(new TemporalPoint(intervalStartTime, intervalEndTime, shift));
                    } catch (InvalidConstraintException e) {
                        throw new DecomposeException(e);
                    }
                }
                return subConstraints;
            }
        };
    }

    @Override
    public boolean satisfy(Condition condition) {
        if (!(condition instanceof Date)) return false;
        Date date = (Date) condition;
        LocalTime localTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
        return localTime.equals(startTime) || (localTime.isAfter(startTime) && localTime.isBefore(endTime));
    }

    @Override
    public Class<? extends Condition> getConditionClass() {
        return DateCondition.class;
    }

    @Override
    public String description() {
        return toString();
    }

    @Override
    public String toString() {
        return "TemporalPoint{" +
                "shift=" + shift +
                ", start=" + startTime +
                ", end=" + endTime +
                '}';
    }
}
