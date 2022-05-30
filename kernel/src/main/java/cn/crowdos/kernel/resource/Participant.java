package cn.crowdos.kernel.resource;

import cn.crowdos.kernel.constraint.Condition;

public interface Participant {

    boolean hasAbility(Class<? extends Condition> conditionClass);

    Condition getAbility(Class<? extends Condition> conditionClass);

    boolean available();

    ResourceStatus getCurrentStatus();
}
