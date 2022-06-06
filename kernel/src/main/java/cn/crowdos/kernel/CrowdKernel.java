package cn.crowdos.kernel;

import cn.crowdos.kernel.algorithms.TrivialAlgoFactory;
import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.system.DuplicateResourceNameException;
import cn.crowdos.kernel.system.SystemResourceCollection;
import cn.crowdos.kernel.system.resource.AlgoContainer;
import cn.crowdos.kernel.system.resource.ParticipantPool;
import cn.crowdos.kernel.system.resource.TaskPool;
import cn.crowdos.kernel.tasksystem.Scheduler;
import cn.crowdos.kernel.tasksystem.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CrowdKernel {

    private boolean initialed = false;
    private static CrowdKernel kernel;

    private SystemResourceCollection systemResourceCollection;

    private CrowdKernel(){}

    public static CrowdKernel getKernel(){
        if (kernel != null) return kernel;
        kernel = new CrowdKernel();
        return kernel;
    }
    public static String version(){
        return "CrowdOS Kernel v1.0";
    }

    public void initial(Object...args){
        systemResourceCollection = new SystemResourceCollection();
        try {
            systemResourceCollection.register(new TaskPool());
            systemResourceCollection.register(new ParticipantPool());
            systemResourceCollection.register(new AlgoContainer(new TrivialAlgoFactory(systemResourceCollection)));
            systemResourceCollection.register(new Scheduler(systemResourceCollection));
        } catch (DuplicateResourceNameException e) {
            throw new RuntimeException(e);
        }
        initialed = true;
    }

    public void initial(){
        initial((Object) null);
    }

    public static void shutdown() {
        kernel = null;
    }

    public SystemResourceCollection getSystemResourceCollection() {
        return systemResourceCollection;
    }

    public boolean isInitialed(){
        return initialed;
    }
    public boolean submitTask(Task task){
        TaskPool resource = systemResourceCollection.getResourceHandler(TaskPool.class).getResource();
        resource.add(task);
        return true;
    }
    public List<Task> getTasks(){
        TaskPool resource = systemResourceCollection.getResourceHandler(TaskPool.class).getResource();
        return new ArrayList<>(resource);
    }
    public Iterator<Task> getTasksIter(){
        TaskPool resource = systemResourceCollection.getResourceHandler(TaskPool.class).getResource();
        return resource.iterator();
    }
    public List<Participant> getTaskAssignmentScheme(Task task){
        Scheduler resource = systemResourceCollection.getResourceHandler(Scheduler.class).getResource();
        return resource.taskAssignment(task);
    }
    public List<Participant> getTaskRecommendationScheme(Task task){
        Scheduler resource = systemResourceCollection.getResourceHandler(Scheduler.class).getResource();
        return resource.taskRecommendation(task);
    }
    public List<Participant> getTaskParticipantSelectionResult(Task task){
        Scheduler resource = systemResourceCollection.getResourceHandler(Scheduler.class).getResource();
        return resource.participantSelection(task);
    }
    public List<Participant> getParticipants(){
        ParticipantPool resource = systemResourceCollection.getResourceHandler(ParticipantPool.class).getResource();
        return new ArrayList<>(resource);
    }
    public Iterator<Participant> getParticipantsIter(){
        ParticipantPool resource = systemResourceCollection.getResourceHandler(ParticipantPool.class).getResource();
        return resource.iterator();
    }
}
