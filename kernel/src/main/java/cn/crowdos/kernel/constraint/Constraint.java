package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.resource.Participant;

import java.util.List;

/**
 *
 * @since 1.0.0
 * @author loyx
 */
public interface Constraint{
    void satisfy();

    void unSatisfy();

    boolean isSatisfied();

    String description();

}
