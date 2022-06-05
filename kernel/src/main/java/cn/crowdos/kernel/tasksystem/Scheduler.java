package cn.crowdos.kernel.tasksystem;

import cn.crowdos.kernel.algorithms.AlgoFactory;
import cn.crowdos.kernel.algorithms.ParticipantSelectionAlgo;
import cn.crowdos.kernel.algorithms.TaskAssignmentAlgo;
import cn.crowdos.kernel.algorithms.TaskRecommendationAlgo;
import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.system.SystemResourceCollection;
import cn.crowdos.kernel.system.SystemResourceHandler;
import cn.crowdos.kernel.system.resource.AlgoContainer;
import cn.crowdos.kernel.system.resource.Resource;

import java.util.List;

public class Scheduler implements Resource<Scheduler> {
    private final ParticipantSelectionAlgo participantSelectionAlgo;
    private final TaskRecommendationAlgo taskRecommendationAlgo;
    private final TaskAssignmentAlgo taskAssignmentAlgo;
    private final AlgoFactory algoFactory;
    private final SystemResourceCollection resourceCollection;

    public Scheduler(SystemResourceCollection collection){
        this.resourceCollection = collection;
        SystemResourceHandler<AlgoFactory> resourceHandler = collection.getResourceHandler(AlgoContainer.class);
        this.algoFactory = resourceHandler.getResource();

        participantSelectionAlgo = algoFactory.getParticipantSelectionAlgo();
        taskRecommendationAlgo = algoFactory.getTaskRecommendationAlgo();
        taskAssignmentAlgo = algoFactory.getTaskAssignmentAlgo();
    }

    public List<Participant> participantSelection(Task task){
        return participantSelectionAlgo.getCandidates(task);
    }

    public List<Participant> taskRecommendation(Task task){
        return taskRecommendationAlgo.getRecommendationScheme(task);
    }

    public List<Participant> taskAssignment(Task task){
        return taskAssignmentAlgo.getAssignmentScheme(task);
    }

    public AlgoFactory getAlgoFactory() {
        return algoFactory;
    }

    @Override
    public SystemResourceHandler<Scheduler> getHandler() {
        return null;
    }
}
