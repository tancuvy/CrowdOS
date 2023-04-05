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

public class TemporalInterval implements Constraint{
    private final LocalTime startTime;
    private final LocalTime endTime;

    public TemporalInterval(String start, String end) throws InvalidConstraintException {
        this(LocalTime.parse(start), LocalTime.parse(end));
    }

    public TemporalInterval(LocalTime startTime, LocalTime endTime) throws InvalidConstraintException{
        this.startTime = startTime;
        this.endTime = endTime;
        if (endTime.isBefore(startTime)){
            throw new InvalidConstraintException(String.format("endTime %s before startTime %s", this.startTime, this.endTime));
        }
    }

    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                try {
                    return Collections.singletonList(new TemporalInterval(startTime, endTime));
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
                        subConstraints.add(new TemporalInterval(intervalStartTime, intervalEndTime));
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
    public String toString() {
        return "TemporalInterval{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public String description() {
        return this.toString();
    }
}
