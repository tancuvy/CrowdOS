package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.Decomposer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleTimeConstraint implements Constraint{

    Date[] dateRange;

    public SimpleTimeConstraint(Date startDate, Date endDate) throws InvalidConstraintException {
        if (startDate.compareTo(endDate) >= 0) throw new InvalidConstraintException();
        dateRange = new Date[]{startDate, endDate};
    }

    public SimpleTimeConstraint(String startDateStr, String endDateStr) throws InvalidConstraintException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date startDate;
        Date endDate;
        try {
            startDate = simpleDateFormat.parse(startDateStr);
            endDate = simpleDateFormat.parse(endDateStr);
        } catch (ParseException e) {
            throw new InvalidConstraintException(e.getCause());
        }
        if (startDate.compareTo(endDate) >= 0) throw new InvalidConstraintException();
        dateRange = new Date[]{startDate, endDate};
    }

    @Override
    public boolean satisfy(Condition condition) {
        if (!(condition instanceof Date)) return false;
        Date date = (Date) condition;
        return dateRange[0].compareTo(date) <= 0 && date.compareTo(dateRange[1]) < 0;
    }

    @Override
    public Class<Condition> getConditionClass() {
        return null;
    }

    @Override
    public String description() {
        return this.toString();
    }

    @Override
    public Decomposer<Constraint> decomposer() {
        return null;
    }
}
