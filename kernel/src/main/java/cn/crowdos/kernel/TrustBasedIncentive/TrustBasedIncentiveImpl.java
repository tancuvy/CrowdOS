package cn.crowdos.kernel.TrustBasedIncentive;

import cn.crowdos.kernel.Scheduler;
import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.system.SystemResourceHandler;
import cn.crowdos.kernel.system.resource.MissionHistory;
import cn.crowdos.kernel.system.resource.Resource;

import java.util.*;

public class TrustBasedIncentiveImpl implements TrustBasedIncentive{

    private final Map<Participant, Integer> trustValues; // 工作者和信誉度的映射
    private final List<Participant> trustedParticipants; // 受信任参与者集合
    private final Map<Participant, Double> rewardValues; // 工作者和报酬的映射

    public TrustBasedIncentiveImpl() {
        rewardValues = new HashMap<>();
        trustValues = new HashMap<>();
        trustedParticipants = new ArrayList<>();
    }
    //将参与者在trustValues中的信誉度加1

    @Override
    public void earnTrust(Participant participant) {
        int trustValue = trustValues.getOrDefault(participant, 0) + 1;
        trustValues.put(participant, trustValue);
    }

    //将参与者在 trustValues 中的信誉度减1
    @Override
    public void penalizeTrust(Participant participant) {
        int trustValue = trustValues.getOrDefault(participant, 0) - 1;
        trustValues.put(participant, trustValue);
    }

    @Override
        public int getTrust(Participant participant) {
        return trustValues.getOrDefault(participant, 0);
    }

    @Override
    public void allocateRewards(double rewards, Task task, Participant firstParticipant, List<Participant> participants) {
        // 假设奖励分配规则为：任务完成者获得80%的奖励，信任链中的参与者共享20%的奖励
        //该第一个完成的工作者得到的报酬
        double firstTrust = getTrust(firstParticipant);
        double firstReward = (rewards * 0.8) + ((firstTrust / 10) * 8);
        rewardValues.put(firstParticipant,firstReward);
        double trustReward = (rewards-firstReward)* 0.2 / participants.size();
        for (Participant participant : participants) {
            if( participant.equals(firstParticipant) ){
                break;
            }else {
                rewardValues.put(participant,isTrustedParticipant(participant) ? trustReward : 0);
            }
        }
    }

    public Map<Participant, Double> IncentiveAssignment() {
        return rewardValues;
    }

    @Override
    public void removeTrustedParticipant(Participant participant) {
        trustedParticipants.remove(participant);
    }

    @Override
    public boolean isTrustedParticipant(Participant participant) {
        return trustedParticipants.contains(participant);
    }

    @Override
    public List<Participant> getTrustedParticipants() {
        return trustedParticipants;
    }

    @Override
    public SystemResourceHandler<TrustBasedIncentive> getHandler() {
        return null;
    }
}


