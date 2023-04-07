package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.resource.Task;

import java.util.Collections;
import java.util.List;

public class DependenceConstraint implements Constraint {
    List<Task> previousTasks;

    public DependenceConstraint(List<Task> previousTasks) {
        this.previousTasks = previousTasks;
    }

    @Override
    public Decomposer<Constraint> decomposer() {
        List<Class<?>> argsClass = Collections.singletonList(previousTasks.getClass());
        List<Object> args = Collections.singletonList(previousTasks);
        return new IndecomposableDecomposerGenerator(argsClass, args, DependenceConstraint.class);
    }

    @Override
    public boolean satisfy() {
        return previousTasks.stream().allMatch(Task::finished);
    }

    @Override
    public boolean satisfy(Condition condition) {
        return satisfy();
    }

    @Override
    public Class<? extends Condition> getConditionClass() {
        return NoneCondition.class;
    }

    @Override
    public String description() {
        return toString();
    }

    @Override
    public String toString() {
        return "DependenceConstraint{" +
                "previousTasks=" + previousTasks +
                '}';
    }
}
