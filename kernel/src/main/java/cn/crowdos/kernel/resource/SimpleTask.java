package cn.crowdos.kernel.resource;

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
            public List<Task> scaleDecompose(int scale) throws DecomposeException {
                throw new DecomposeException("nonsupport");
            }

        };
    }
}
