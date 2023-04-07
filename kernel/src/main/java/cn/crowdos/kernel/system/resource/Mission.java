package cn.crowdos.kernel.system.resource;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mission {
    private final Task task;
    private final List<Participant> participants;
    private final List<Date> submitTimes;
    private final List<Participant> submitParticipants;

    enum MissionStatus {
        UNFINISHED,
        FINISHED
    }
    MissionStatus missionStatus;

    public Mission(Task task, List<Participant> participants) {
        this.task = task;
        this.participants = participants;
        this.submitTimes = new ArrayList<>();
        this.submitParticipants = new ArrayList<>();
        this.missionStatus = MissionStatus.UNFINISHED;
    }

    public Participant getFirstSubmitParticipant(){
        return submitParticipants.get(0);
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void updateSubmit(Participant participant) throws MissionUpdateException {
        updateSubmit(participant, new Date());
    }

    public void updateSubmit(Participant participant, Date submitTime) throws MissionUpdateException {
        if (!participants.contains(participant) || submitParticipants.contains(participant)){
            throw new MissionUpdateException();
        }
        submitParticipants.add(participant);
        submitTimes.add(submitTime);
        if (submitParticipants.size() == participants.size()){
            task.setTaskStatus(Task.TaskStatus.FINISHED);
            missionStatus = MissionStatus.FINISHED;
        }
    }

    public boolean belongTo(Task task){
        return this.task.equals(task);
    }

    public boolean involved(Participant participant){
        return this.participants.contains(participant);
    }
}
