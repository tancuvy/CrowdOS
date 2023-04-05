package cn.crowdos.kernel.system.resource;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.system.SystemResourceHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MissionHistory implements Resource<MissionHistory>{
    ArrayList<Mission> missions;

    public MissionHistory() {
        this.missions = new ArrayList<>();
    }

    public void newMission(Task task, List<Participant> participants){
        missions.add(new Mission(task, participants));
    }

    public List<Mission> getUnfinishedMissions(){
        return missions.stream()
                .filter(mission -> mission.missionStatus == Mission.MissionStatus.UNFINISHED)
                .collect(Collectors.toList());
    }

    public List<Mission> getFinishedMissions(){
        return missions.stream()
                .filter(mission -> mission.missionStatus == Mission.MissionStatus.FINISHED)
                .collect(Collectors.toList());
    }

    public List<Mission> getMissionsByTask(Task task){
        return missions.stream()
                .filter(mission -> mission.belongTo(task))
                .collect(Collectors.toList());
    }

    public List<Mission> getMissionsByParticipant(Participant participant){
        return missions.stream()
                .filter(mission -> mission.involved(participant))
                .collect(Collectors.toList());
    }

    @Override
    public SystemResourceHandler<MissionHistory> getHandler() {
        MissionHistory history = this;
        return new SystemResourceHandler<MissionHistory>() {
            @Override
            public MissionHistory getResourceView() {
                return history;
            }

            @Override
            public MissionHistory getResource() {
                return history;
            }
        };
    }
}
