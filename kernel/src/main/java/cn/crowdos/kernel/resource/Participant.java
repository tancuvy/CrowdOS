package cn.crowdos.kernel.resource;

import cn.crowdos.kernel.constraint.Condition;

public interface Participant {

    enum ParticipantStatus {
        AVAILABLE,
        BUSY,
        DISABLED,
    }

    ParticipantStatus getStatus();

    void setStatus(ParticipantStatus status);

    boolean hasAbility(Class<? extends Condition> conditionClass);

    Condition getAbility(Class<? extends Condition> conditionClass);

    boolean available();

}
