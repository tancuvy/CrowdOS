package cn.crowdos.kernel.tasksystem;

import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.constraint.Condition;
import cn.crowdos.kernel.constraint.Constraint;
import cn.crowdos.kernel.resource.Participant;

import java.util.List;

public abstract class AbstractTask implements Task{

    protected final List<Constraint> constraints;
    protected final TaskDistributionType taskDistributionType;
    protected TaskStatus status;

    protected AbstractTask(List<Constraint> constraints, TaskDistributionType taskDistributionType) {
        this.constraints = constraints;
        this.taskDistributionType = taskDistributionType;
    }


    abstract public Decomposer<Task> decomposer();

    @Override
    public TaskDistributionType getTaskDistributionType() {
        return taskDistributionType;
    }

    @Override
    public TaskStatus getTaskStatus() {
        return status;
    }

    @Override
    public void setTaskStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public List<Constraint> constraints() {
        return constraints;
    }

    @Override
    public boolean canAssignTo(Participant participant) {
        for (Constraint constraint : constraints) {
            Class<? extends Condition> conditionClass = constraint.getConditionClass();
            if(participant.hasAbility(conditionClass)){
                if (!constraint.satisfy(participant.getAbility(conditionClass)))
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean assignable() {
        return status == TaskStatus.READY;
    }

    @Override
    public boolean finished() {
        return status == TaskStatus.FINISHED;
    }

}
