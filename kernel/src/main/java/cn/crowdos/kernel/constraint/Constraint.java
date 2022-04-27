package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.resource.Participant;

import java.util.List;

/**
 * <p>In <i>Mobile CrowdSensing</i> (MCS), there are a lots of constraints.
 * For task, there exits Spatial constraint, Temporal constraint or
 * Sampling constraint. For participant, there are some special
 * constraints, such as the number of tasks that participants can
 * perform simultaneously, or the maximum distance a participant can
 * move. For sensor, there are constraints on sensor accuracy and type.</p>
 *
 * <p>The constraints in any real MCS problem need to be mapped directly to
 * a class that implements the {@code Constraint} interface or extend from
 * some abstract Constraint class, such as xxx</p>
 *
 * <p>For instance, there are some constraints in the paper "
 * <a href="https://doi.org/10.1109/HPCC-DSS-SmartCity-DependSys53884.2021.00360">
 * HuMachineSensing: A Novel Mobile Crowdsensing Framework with Robust Task
 * Allocation Algorithm</a>". The Constraints of the model in this paper are
 * as follows:
 * <ol>
 *     <li>Participants need to complete the task within the required time.</li>
 *     <li>Participants should have sensors that can satisfy the task requirements.</li>
 *     <li>The total movement path of participants should not exceed the limit.</li>
 *     <li>Each subtask should be performed γ times independently by different participants.</li>
 * </ol>
 *
 * Each Constraint can map to a class that implement interface {@code Constraint}. For example, the
 * code for Constraint 1 is shown below.
 *
 * <pre>{@code
 *   import cn.crowdos.kernel.constraint.Constraint;
 *
 *   public class TimeConstraint implement Constraint {
 *      @override
 *      void satisfy(){...}
 *
 *      @override
 *      void unSatisfy(){...}
 *
 *      @override
 *      boolean isSatisfied(){...}
 *
 *      @override
 *      String description(){
 *          return "Time Constraint";
 *      }
 *
 *      ... // other methods
 *   }
 * }</pre>
 * After that the constraint class will be involved in the kernel's task
 * allocation process.
 * </p>
 *
 * <p>Note that the class that implements this interface contains the state,
 * and the method {@link Constraint#satisfy()} and {@link Constraint#unSatisfy()}
 * are <i>not</i> idempotent method.</p>
 *
 * @since 1.0.0
 * @author loyx
 */
public interface Constraint{
    /**
     * satisfy this Constraint and set the satisfied state.
     */
    void satisfy();

    /**
     * undo satisfy operation, this method will affect satisfied state.
     */
    void unSatisfy();

    /**
     * Return {@code true} if this constraint is satisfied.
     * @return {@code true} if this constraint is satisfied
     */
    boolean isSatisfied();

    /**
     * Return the description of this constraint.
     * @return the description of this constraint
     */
    String description();

}
