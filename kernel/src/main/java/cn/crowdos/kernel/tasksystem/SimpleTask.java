package cn.crowdos.kernel.tasksystem;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.constraint.Constraint;

import java.util.*;

public class SimpleTask extends AbstractTask{

    public SimpleTask(List<Constraint> constraints, TaskDistributionType taskDistributionType) {
        super(constraints, taskDistributionType);
    }

    @Override
    public Decomposer<Task> decomposer() {
        SimpleTask simpleTask = this;
        return new Decomposer<Task>() {
            @Override
            public List<Task> trivialDecompose() {
                return Collections.singletonList(simpleTask);
            }

            @Override
            public Iterator<Task> trivialDecomposerIterator() {
                return new Iterator<Task>() {
                    boolean singleton = false;
                    @Override
                    public boolean hasNext() {
                        if (!singleton) {
                            singleton = true;
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public Task next() {
                        return simpleTask;
                    }
                };
            }

            @Override
            public List<Task> scaleDecompose(int scale) throws DecomposeException {
                throw new DecomposeException("nonsupport");
            }

            @Override
            public Iterator<Task> scaleDecomposerIterator(int scale) throws DecomposeException {
                throw  new DecomposeException("nonsupport");
            }
        };
    }
}
