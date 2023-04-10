package cn.crowdos.kernel;

import cn.crowdos.kernel.algorithms.*;
import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.system.SystemResourceCollection;
import cn.crowdos.kernel.system.SystemResourceHandler;
import cn.crowdos.kernel.system.resource.AlgoContainer;
import cn.crowdos.kernel.system.resource.Resource;
import cn.crowdos.kernel.system.resource.TaskPool;

import java.util.ArrayList;
import java.util.List;

public class Scheduler implements Resource<Scheduler> {
    // A private variable that is used to store the participant selection algorithm.
    private ParticipantSelectionAlgo participantSelectionAlgo;
    // A private variable that is used to store the task recommendation algorithm.
    private TaskRecommendationAlgo taskRecommendationAlgo;
    // A private variable that is used to store the task assignment algorithm.
    private TaskAssignmentAlgo taskAssignmentAlgo;
    // A private variable that is used to store the algorithm factory.
    private AlgoFactory algoFactory;
    // Used to store the resource collection.
    private final SystemResourceCollection resourceCollection;

    // The constructor of the Scheduler class. It is used to initialize the scheduler.
    public Scheduler(SystemResourceCollection collection){
        this.resourceCollection = collection;
        SystemResourceHandler<AlgoFactory> resourceHandler = collection.getResourceHandler(AlgoContainer.class,"Default");
        this.algoFactory = resourceHandler.getResource();

        participantSelectionAlgo = algoFactory.getParticipantSelectionAlgo();
        taskRecommendationAlgo = algoFactory.getTaskRecommendationAlgo();
        taskAssignmentAlgo = algoFactory.getTaskAssignmentAlgo();
    }
    public void SetAlgoFactory(String name){
        SystemResourceHandler<AlgoFactory> resourceHandler = resourceCollection.getResourceHandler(AlgoContainer.class,name);
        algoFactory = resourceHandler.getResource();
        participantSelectionAlgo = algoFactory.getParticipantSelectionAlgo();
        taskRecommendationAlgo = algoFactory.getTaskRecommendationAlgo();
        taskAssignmentAlgo = algoFactory.getTaskAssignmentAlgo();
    }


    /**
     *  For each task in the task pool, recommend a list of participants to complete the task
     *
     * @return A list of lists of participants.
     */
    public List<List<Participant>> recommendTasks(){
        TaskPool resourceView = resourceCollection.getResourceHandler(TaskPool.class).getResourceView();
        List<List<Participant>> recommendScheme = new ArrayList<>(resourceView.size());
        for (Task task : resourceView) {
            recommendScheme.add(taskRecommendation(task));
        }
        return recommendScheme;
    }

    /**
     *  For each task in the task pool, assign a list of participants to it
     *
     * @return A list of lists of participants.
     */
    public List<List<Participant>> assignTasks(){
        TaskPool resourceView = resourceCollection.getResourceHandler(TaskPool.class).getResourceView();
        List<List<Participant>> assignmentScheme = new ArrayList<>(resourceView.size());
        for (Task task : resourceView) {
            assignmentScheme.add(taskAssignment(task));
        }
        return assignmentScheme;
    }

    /**
     * The participant selection algorithm is used to get the candidates for a task.
     *
     * @param task The task for which the participants are to be selected.
     * @return A list of participants.
     */
    public List<Participant> participantSelection(Task task){
        return participantSelectionAlgo.getCandidates(task);
    }
    public List<List<Participant>> participantSelection(ArrayList<Task> tasks){
        return participantSelectionAlgo.getCandidates(tasks);
    }
    /**
     * This function returns a list of participants that are recommended for a given task.
     *
     * @param task The task for which you want to get the recommendations.
     * @return A list of participants.
     */
    public List<Participant> taskRecommendation(Task task){
        return taskRecommendationAlgo.getRecommendationScheme(task);
    }
    public List<List<Participant>> taskRecommendation(ArrayList<Task> tasks){
        return taskRecommendationAlgo.getRecommendationScheme(tasks);
    }
    /**
     * This function returns a list of participants that are assigned to a task.
     *
     * @param task The task object that needs to be assigned to participants.
     * @return A list of participants.
     */
    public List<Participant> taskAssignment(Task task){
        return taskAssignmentAlgo.getAssignmentScheme(task);
    }
    public List<List<Participant>> taskAssignment(ArrayList<Task> tasks){
        return taskAssignmentAlgo.getAssignmentScheme(tasks);
    }
    /**
     * This function returns the AlgoFactory object that was created in the constructor.
     *
     * @return The algoFactory object.
     */
    public AlgoFactory getAlgoFactory() {
        return algoFactory;
    }

    @Override
    // A method that returns a SystemResourceHandler object.
    public SystemResourceHandler<Scheduler> getHandler() {
        Scheduler scheduler = this;
        return new SystemResourceHandler<Scheduler>() {
            @Override
            public Scheduler getResourceView() {
                return scheduler;
            }

            @Override
            public Scheduler getResource() {
                return scheduler;
            }
        };
    }
}
