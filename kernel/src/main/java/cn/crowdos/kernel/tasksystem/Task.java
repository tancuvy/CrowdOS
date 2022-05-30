package cn.crowdos.kernel.tasksystem;

import cn.crowdos.kernel.Decomposable;
import cn.crowdos.kernel.constraint.Constraint;
import cn.crowdos.kernel.resource.Participant;

import java.util.List;

public interface Task extends Decomposable<Task> {

    enum TaskDistributionType{
        ASSIGNMENT,
        RECOMMENDATION,
    }

    enum TaskStatus {
        READY,
        IN_PROGRESS,
        FINISHED,
    }

    TaskDistributionType getTaskDistributionType();

    TaskStatus getTaskStatus();
    void setTaskStatus(TaskStatus status);

    List<Constraint> constraints();

    boolean canAssignTo(Participant participant);

    boolean assignable();

    boolean finished();

}
