package cn.crowdos.kernel.algorithms;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.tasksystem.Task;

import java.util.List;

@FunctionalInterface
public interface TaskRecommendationAlgo {
    List<Participant> getRecommendationScheme(Task task);
}
