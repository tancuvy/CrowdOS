package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;

import javax.xml.crypto.Data;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DateDeadline implements Constraint{
    private final LocalDate endDate;

    public DateDeadline(LocalDate endDate) throws InvalidConstraintException{
        this.endDate = endDate;
        if(endDate.isBefore(LocalDate.now())){
            throw new InvalidConstraintException(String.format("endDate %s is before currentDate %s",this.endDate,LocalDate.now()));
        }
    }

    public DateDeadline(String endDate) throws InvalidConstraintException{
        this(LocalDate.parse(endDate));
    }

    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                try{
                    return Collections.singletonList(new DateDeadline(endDate));
                } catch (InvalidConstraintException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public List<Constraint> scaleDecompose(int scale) throws DecomposeException {
                if (scale<0) throw new DecomposeException("invalid decompose scale");
                if (scale==1) return trivialDecompose();
                Duration duration = Duration.between(LocalDate.now(),endDate);
                long totalDays = duration.toDays();
                long daysPerInterval = totalDays/scale;
                List<Constraint> subConstraints = new ArrayList<>(scale);
                for(int i=0;i<scale;i++){
                    LocalDate intervalStartDate = LocalDate.now().plusDays(i*daysPerInterval);
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
        return localDate.equals(LocalDate.now()) || (localDate.isAfter(LocalDate.now())) &&localDate.isBefore(endDate);
    }

    @Override
    public Class<? extends Condition> getConditionClass() {
        return DateCondition.class;
    }

    @Override
    public String toString(){
        return "DateDeadline{" +
                "endDate=" + endDate +
                '}';
    }

    @Override
    public String description() {
        return toString();
    }
}
