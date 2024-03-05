/*
 * Copyright 2019-2024 CrowdOS_Group, Inc. <https://github.com/crowdosNWPU/CrowdOS>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * imitations under the License.
 */

package cn.crowdos.kernel.algorithms;

import cn.crowdos.kernel.constraint.Constraint;
import cn.crowdos.kernel.constraint.Coordinate;
import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.system.SystemResourceCollection;
import cn.crowdos.kernel.system.resource.ParticipantPool;
import cn.crowdos.kernel.constraint.POIConstraint;

import java.util.*;
import java.util.stream.Collectors;


public class PTMostFactory extends AlgoFactoryAdapter {

    public PTMostFactory(SystemResourceCollection resourceCollection) {
        super(resourceCollection);
    }

    /**
     * 单任务分配
     *
     * @return {@code TaskAssignmentAlgo}
     */
    @Override
    public TaskAssignmentAlgo getTaskAssignmentAlgo() {
        return new TaskAssignmentAlgo() {
            @Override
            public List<List<Participant>> getAssignmentScheme(ArrayList<Task> tasks) {
                if(resourceCollection.getResourceHandler(ParticipantPool.class) == null){
                    throw new NullPointerException("participantpool is null");
                }
                    ParticipantPool participants = resourceCollection.getResourceHandler(ParticipantPool.class).getResourceView();


                //所有任务的位置信息
                List<Coordinate> taskLocations = new ArrayList<>();
                //所有候选者的位置信息
                List<Coordinate> candidateLocations = new ArrayList<>();
                //保留所有候选者
                List<Participant> canditates = new ArrayList<>();

                //遍历tasks
                for (Task task : tasks){
                    List<Constraint> taskConstraint = task.constraints().stream()
                            .filter(constraint -> constraint instanceof POIConstraint)
                            .collect(Collectors.toList());
                    if(taskConstraint.size() != 1){
                        return null;
                    }
                    Coordinate taskLocation = (Coordinate) taskConstraint.get(0);
                    //将taskLocation加入taskLocations
                    taskLocations.add(taskLocation);
                }


                for (Participant participant:participants){
                    for (Task task:tasks){
                        if(!task.canAssignTo(participant)){
                            continue;
                        }
                    }
                    canditates.add(participant);
                }

                int taskNum = tasks.size();
                int workerNum = canditates.size();

                //遍历candidates,将其位置信息加入candidateLocations
                for (Participant candidate : canditates){
                    Coordinate candidateLocation = (Coordinate) candidate.getAbility(Coordinate.class);
                    candidateLocations.add(candidateLocation);
                }

                //任务-工作者距离矩阵
                double[][] distanceMatrix = new double[workerNum][taskNum];
                //任务距离矩阵
                double[][] taskDistanceMatrix = new double[taskNum][taskNum];

                //根据candidateLocations和taskLocations计算距离矩阵
                for (int i = 0; i < workerNum; i++){
                    for (int j = 0; j < taskNum; j++){
                        distanceMatrix[i][j] = candidateLocations.get(i).euclideanDistance(taskLocations.get(j));
                    }
                }

                //根据taskLocations计算任务距离矩阵
                for (int i = 0; i < taskNum; i++){
                    for (int j = 0; j < taskNum; j++){
                        taskDistanceMatrix[i][j] = taskLocations.get(i).euclideanDistance(taskLocations.get(j));
                    }
                }

                //默认每个任务只需要一个参与者
                int[] p =new int[taskNum];
                for (int i =0; i < taskNum; i++){
                    p[i] = 1;
                }

                //创建算法实体
                PT_Most pt_most = new PT_Most(workerNum, taskNum, distanceMatrix, taskDistanceMatrix, p, 1);
                pt_most.taskAssign();

                //保存任务分配结果
                List<List<Participant>> assignmentScheme = new ArrayList<>(taskNum);


                /*
                 *
                将pt_most.getAssignMap()中的数据加入assignmentScheme,由于原算法结果为map<worker,List<task>>,
                而我们的内核结果为task对应的worker，所以需要转换
                **/
                for (Map.Entry<Integer, List<Integer>> entry : pt_most.getAssignMap().entrySet()){
                    Participant participant = canditates.get(entry.getKey());

                    for (Integer taskId : entry.getValue()){
                            assignmentScheme.get(taskId).add(participant);
                    }

                }


                return assignmentScheme;
            }

            @Override
            public List<Participant> getAssignmentScheme(Task task) {
                // prepare args
                ParticipantPool participants = resourceCollection.getResourceHandler(ParticipantPool.class).getResourceView();

                int taskNum = 1;

                List<Constraint> taskLocation = task.constraints().stream()
                        .filter(constraint -> constraint instanceof POIConstraint)
                        .collect(Collectors.toList());
                if (taskLocation.size() != 1) {
                    return null;
                }
                List<Participant> candidate = participants.stream()
                        .filter(task::canAssignTo)
                        .collect(Collectors.toList());
                int workerNum = candidate.size();


                Coordinate tLocation = (Coordinate) taskLocation.get(0);
                double[][] distanceMatrix = new double[workerNum][taskNum];
                for (int i = 0; i < candidate.size(); i++) {
                    Participant worker = candidate.get(i);
                    Coordinate wLocation = (Coordinate) worker.getAbility(Coordinate.class);
                    distanceMatrix[i][0] = wLocation.euclideanDistance(tLocation);
                }

                double[][] taskDistanceMatrix = new double[][]{{1}};


                // create algorithm instance
                PT_Most pt_most = new PT_Most(workerNum, taskNum, distanceMatrix, taskDistanceMatrix, new int[]{1}, 1);
                pt_most.taskAssign();

                // parser result
                List<Participant> assignmentScheme = new ArrayList<>();
                pt_most.getAssignMap().keySet().forEach(participantId -> assignmentScheme.add(participants.get(participantId)));
                return assignmentScheme;
            }
        };

    }

}
