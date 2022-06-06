package cn.crowdos.kernel;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.system.SystemResourceCollection;

import java.util.List;

public interface CrowdKernel {
    // CrowdKernel APIs
    boolean isInitialed();
    void initial(Object...args);
    void initial();
    SystemResourceCollection getSystemResourceCollection() ;
    boolean submitTask(Task task);
    boolean registerParticipant(Participant participant);
    List<Task> getTasks();
    List<Participant> getTaskAssignmentScheme(Task task);
    List<Participant> getTaskRecommendationScheme(Task task);
    List<Participant> getTaskParticipantSelectionResult(Task task);
    List<Participant> getParticipants();

}
