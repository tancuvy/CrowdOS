package cn.crowdos.kernel.TrustBasedIncentive;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.system.resource.Resource;

import java.util.List;
import java.util.Map;

public interface TrustBasedIncentive extends Resource<TrustBasedIncentive> {


    Map<Participant, Double> IncentiveAssignment();

    // 信任值计算
    void earnTrust(Participant participant);
    void penalizeTrust(Participant participant);
    int getTrust(Participant participant);

    // 奖励分配
    void allocateRewards(double rewards, Task task, Participant firstParticipant, List<Participant> participants);

    // 信任链
    void removeTrustedParticipant(Participant participant);
    boolean isTrustedParticipant(Participant participant);
    List<Participant> getTrustedParticipants();
}
