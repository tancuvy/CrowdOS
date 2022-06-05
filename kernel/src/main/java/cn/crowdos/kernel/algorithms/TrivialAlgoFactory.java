package cn.crowdos.kernel.algorithms;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.system.SystemResourceCollection;
import cn.crowdos.kernel.system.resource.ParticipantPool;
import cn.crowdos.kernel.tasksystem.Task;

import java.util.LinkedList;
import java.util.List;

public class TrivialAlgoFactory implements AlgoFactory{

    private final SystemResourceCollection resourceCollection;

    public TrivialAlgoFactory(SystemResourceCollection resourceCollection){
        this.resourceCollection = resourceCollection;
    }
    @Override
    public ParticipantSelectionAlgo getParticipantSelectionAlgo() {

        return new ParticipantSelectionAlgo() {
            final ParticipantPool participantPool;
            {
                participantPool = resourceCollection.getResourceHandler(ParticipantPool.class).getResourceView();
            }
            @Override
            public List<Participant> getCandidates(Task task) {
                List<Participant> candidate = new LinkedList<>();
                for (Participant participant : participantPool) {
                    if (participant.available() && task.canAssignTo(participant)){
                        candidate.add(participant);
                    }
                }
                return candidate;
            }
        };
    }

    @Override
    public TaskRecommendationAlgo getTaskRecommendationAlgo() {
        return null;
    }

    @Override
    public TaskAssignmentAlgo getTaskAssignmentAlgo() {
        return null;
    }
}
