package cn.crowdos.kernel.algorithms;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;

import java.util.List;

@FunctionalInterface
public interface ParticipantSelectionAlgo {
    List<Participant> getCandidates(Task task);
}
