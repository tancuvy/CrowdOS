package cn.crowdos.kernel.algorithms;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;

import java.util.List;

@FunctionalInterface
public interface TaskRecommendationAlgo {
    /**
     * Given a task, return a list of participants that are recommended to work on the task.
     *
     * @param task The task for which you want to get the recommendation scheme.
     * @return A list of participants.
     */
    List<Participant> getRecommendationScheme(Task task);
}
