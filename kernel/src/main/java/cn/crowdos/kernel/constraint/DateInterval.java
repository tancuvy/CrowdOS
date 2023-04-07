package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DateInterval implements Constraint{
    private final LocalDate startDate;
    private final LocalDate endDate;

    public DateInterval(LocalDate startDate, LocalDate endDate) throws InvalidConstraintException{
        this.startDate = startDate;
        this.endDate = endDate;
        if(endDate.isBefore(startDate)){
            throw new InvalidConstraintException(String.format("endDate %s is before startDate %s",this.endDate,this.startDate));
        }
    }

    public DateInterval(String start, String end) throws InvalidConstraintException {
        this(LocalDate.parse(start), LocalDate.parse(end));
    }


    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                try{
                    return Collections.singletonList(new DateInterval(startDate,endDate));
                }catch (InvalidConstraintException e){
                    throw new RuntimeException(e);
                }
            }

            @Override
            public List<Constraint> scaleDecompose(int scale) throws DecomposeException {
                if (scale<0) throw new DecomposeException("invalid decompose scale");
                if (scale==1) return trivialDecompose();
                Duration duration = Duration.between(startDate,endDate);
                long totalDays = duration.toDays();
                long daysPerInterval = totalDays/scale;
                List<Constraint> subConstraints = new ArrayList<>(scale);
                for(int i=0;i<scale;i++){
                    LocalDate intervalStartDate = startDate.plusDays(i*daysPerInterval);
                    LocalDate intervalEndDate = intervalStartDate.plusDays(daysPerInterval);
                    try{
                        subConstraints.add(new DateInterval(intervalStartDate,intervalEndDate));
                    }catch(InvalidConstraintException e){
                        throw new DecomposeException(e);
                    }
                }
                return subConstraints;
            }
        };
    }

    @Override
    public boolean satisfy(Condition condition) {
        if(!(condition instanceof Date)) return false;
        Date date = (Date) condition;
        LocalDate localDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
        return localDate.equals(startDate) || (localDate.isAfter(startDate)) &&localDate.isBefore(endDate);
    }

    @Override
    public Class<? extends Condition> getConditionClass() {
        return DateCondition.class;
    }

    @Override
    public String toString() {
        return "DataInterval{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @Override
    public String description() {
        return this.toString();
    }
}
