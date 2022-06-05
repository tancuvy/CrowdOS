package cn.crowdos.kernel.algorithms;

public interface AlgoFactory {
    ParticipantSelectionAlgo getParticipantSelectionAlgo();
    TaskRecommendationAlgo getTaskRecommendationAlgo();
    TaskAssignmentAlgo getTaskAssignmentAlgo();
}
