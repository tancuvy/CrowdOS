package cn.crowdos.kernel;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.system.SystemResourceCollection;
import cn.crowdos.kernel.resource.Task;

import java.util.Iterator;
import java.util.List;

public interface CrowdKernel {
    // CrowdKernel APIs
    boolean isInitialed();
    void initial(Object...args);
    void initial();
    SystemResourceCollection getSystemResourceCollection() ;
    boolean submitTask(Task task);
    List<Task> getTasks();
    Iterator<Task> getTasksIter();
    List<Participant> getTaskAssignmentScheme(Task task);
    List<Participant> getTaskRecommendationScheme(Task task);
    List<Participant> getTaskParticipantSelectionResult(Task task);
    List<Participant> getParticipants();
    Iterator<Participant> getParticipantsIter();

}
