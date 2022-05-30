package cn.crowdos.kernel.resource;

import cn.crowdos.kernel.constraint.Condition;

import java.util.List;

public abstract class AbstractParticipant implements Participant{


    protected ParticipantStatus status;

    public ParticipantStatus getStatus() {
        return status;
    }

    public void setStatus(ParticipantStatus status) {
        this.status = status;
    }
    public abstract boolean hasAbility(Class<? extends Condition> conditionClass);

    public abstract Condition getAbility(Class<? extends Condition> conditionClass);

    @Override
    public boolean available() {
        return status == ParticipantStatus.AVAILABLE;
    }

    @Override
    public ParticipantStatus getCurrentStatus() {
        return status;
    }
}
