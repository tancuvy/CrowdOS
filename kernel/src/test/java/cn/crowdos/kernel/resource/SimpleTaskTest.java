package cn.crowdos.kernel.resource;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.constraint.Condition;
import cn.crowdos.kernel.constraint.InvalidConstraintException;
import cn.crowdos.kernel.constraint.SimpleTimeConstraint;
import cn.crowdos.kernel.constraint.wrapper.DateCondition;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SimpleTaskTest {
    Task task;
    {
        SimpleTimeConstraint timeConst = null;
        try {
            timeConst = new SimpleTimeConstraint("2022.6.1", "2022.6.5");
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
        task = new SimpleTask(Collections.singletonList(timeConst), Task.TaskDistributionType.RECOMMENDATION);
    }

    @Test
    void decomposer() {
        Decomposer<Task> decomposer = task.decomposer();
        try {
            for (Task sub : decomposer.decompose(5)) {
                System.out.println(sub);
            }
        } catch (DecomposeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getTaskDistributionType() {
        System.out.println(task.getTaskDistributionType());
    }

    @Test
    void getTaskStatus() {
        System.out.println(task.getTaskStatus());
    }

    @Test
    void constraints() {
        System.out.println(task.constraints());
    }

    @Test
    void canAssignTo() {
        class TimeParticipant extends AbstractParticipant{
            @ability
            final DateCondition activeTime;

            TimeParticipant(String time) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
                try {
                    this.activeTime = new DateCondition(format.parse(time).getTime());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public boolean hasAbility(Class<? extends Condition> conditionClass) {
                return conditionClass==DateCondition.class;
            }

            @Override
            public Condition getAbility(Class<? extends Condition> conditionClass) {
                if (!hasAbility(conditionClass))
                    return null;
                else return activeTime;
            }
        }

        TimeParticipant p1 = new TimeParticipant("2022.6.3");
        TimeParticipant p2 = new TimeParticipant("2022.6.10");
        assertTrue(task.canAssignTo(p1));
        assertFalse(task.canAssignTo(p2));
    }

    @Test
    void assignable() {
        System.out.println(task.assignable());
    }

    @Test
    void finished() {
        System.out.println(task.finished());
    }
}