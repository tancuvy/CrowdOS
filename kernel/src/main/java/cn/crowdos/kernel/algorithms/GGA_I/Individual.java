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

package cn.crowdos.kernel.algorithms.GGA_I;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.*;

/**
 * 个体
 * 执行Serializable接口为了”序列化实现深拷贝”
 *
 * @author wushengjie
 */

public class Individual implements Serializable {


    private Map<Integer, List<Integer>> assignMap = new HashMap<>(); //染色体序列，即任务分配结果

    private int workerNum; //工人数量

    private int taskNum; //任务数量

    private int[] p; //每个任务需要多少个工人执行

    private int q; //每个工人最多执行多少个任务

    double[][] distanceMatrix; //距离矩阵

    double[][] taskDistanceMatrix; //任务距离矩阵

    private double distance; //路程

    private double fitness; //适应度（即总路程越短，适应度越高，取fitness=1.0/distance）

    private double survivalRate; //生存率（适应度越高，生存率越高）


    /**
     */
    public Individual(int workerNum,int taskNum,double[][] distanceMatrix,double[][] taskDistanceMatrix,int[] p,int q) {

        this.workerNum = workerNum;
        this.taskNum = taskNum;
        this.distanceMatrix = distanceMatrix;
        this.taskDistanceMatrix = taskDistanceMatrix;
        this.p = p;
        this.q = q;


        //初始化Map
        for (int i = 0; i < workerNum; i++) {
            this.assignMap.put(i, new ArrayList<>());

        }

        initGenesByRandom();

    }

    /**
     * @param greedyType 0：最近邻贪心算法
     */
    public Individual(int workerNum,int taskNum,double[][] distanceMatrix,double[][] taskDistanceMatrix,
                      int[] p,int q,int greedyType) {
        this.workerNum = workerNum;
        this.taskNum = taskNum;
        this.distanceMatrix = distanceMatrix;
        this.taskDistanceMatrix = taskDistanceMatrix;
        this.p = p;
        this.q = q;


        //初始化Map
        for (int i = 0; i < workerNum; i++) {
            this.assignMap.put(i, new ArrayList<>());

        }

        initGenesByNearestFirst();

    }

    /**
     * 随机初始化基因
     */
    public void initGenesByRandom() {

        Random random = new SecureRandom();
        int taskIndex;
        int workerIndex;

        //将任务分给工人
        for (int i = 0; i < taskNum; i++) {

            for (int j = 0; j < p[i]; j++) {
                //随机选择一个工人
                workerIndex = random.nextInt(workerNum);


                if(assignMap.get(workerIndex).size() >= q || assignMap.get(workerIndex).contains(i)){
                    j--;

                }
                else {
                    assignMap.get(workerIndex).add(i);

                }

            }
        }


        //遍历assignMap，打乱list
        for (int i = 0; i < workerNum; i++) {
            Collections.shuffle(assignMap.get(i));
        }


    }


    /**
     * 根据NearestFirst初始化基因
     */
    public void initGenesByNearestFirst(){

        //复制一份距离矩阵
        double[][] distanceMatrixCopy = new double[workerNum][taskNum];
        for (int i = 0; i < workerNum; i++) {
            for (int j = 0; j < taskNum; j++) {
                distanceMatrixCopy[i][j] = distanceMatrix[i][j];
            }
        }
        NearestFirst nearestFirst = new NearestFirst(workerNum, taskNum, distanceMatrixCopy, p, q);
        nearestFirst.taskAssign();
        Map<Integer,List<Integer>> assignMapTemp = nearestFirst.getAssignMap();
        //复制assignMapTemp到assignMap
        for (int i = 0; i < workerNum; i++) {
            for (int j = 0; j < assignMapTemp.get(i).size(); j++) {
                assignMap.get(i).add(assignMapTemp.get(i).get(j));
            }
        }

    }



    /**
     * 计算个体的路程距离和适应度
     */
    public void calculateDistanceAndFitness() {

        distance = 0;
        //遍历assignMap，计算距离
        for (int i = 0; i < workerNum; i++) {
            List<Integer> taskList = assignMap.get(i);
            if(taskList.size() == 0){
                continue;
            }
            //计算工人i到第一个任务的距离
            distance += distanceMatrix[i][taskList.get(0)];
            //计算工人i从第一个任务到最后一个任务的距离
            for (int j = 0; j < taskList.size() - 1; j++) {
                distance += taskDistanceMatrix[taskList.get(j)][taskList.get(j+1)];
            }
            //计算工人i从最后一个任务到工人i的距离
            distance += distanceMatrix[i][taskList.get(taskList.size()-1)];
        }

        //计算适应度
        fitness = 1.0 / distance;
    }

    /**
     * FIXME 变异操作1：随机将一个工人的一个任务与另一个工人的一个任务交换  目前有BUG，陷入死循环
     */
    public void mutation() {

        Random random = new SecureRandom();
        int workerIndex1;
        int workerIndex2;

        //随机选择一个工人1，任务列表非空
        while(true){
            workerIndex1 = random.nextInt(workerNum);
            if (assignMap == null){
                    throw new NullPointerException("workerindex is null");
            }
            if (assignMap.get(workerIndex1).size() > 0){
                break;
            }
        }

        while(true) {
            //随机选择一个工人2
            workerIndex2 = random.nextInt(workerNum);

            //避免重复选择
            while (workerIndex1 == workerIndex2) {
                workerIndex2 = random.nextInt(workerNum);
            }

            //获取工人1的第一个任务
            int taskIndex = assignMap.get(workerIndex1).get(0);

            //如果工人2的任务列表中不包含工人1的第一个任务，则跳出循环，否则继续选择工人2
            if (assignMap.get(workerIndex2).contains(taskIndex)) {
                continue;
            }
            assignMap.get(workerIndex2).add(0, taskIndex);
            break;
        }


    }


    /**
     *变异操作2：随机选择一个工人，打乱其任务列表
     */
    public void mutation2() {
        Random random = new SecureRandom();
        int workerIndex;
        while(true){
            workerIndex = random.nextInt(workerNum);
            if (assignMap.get(workerIndex).size() > 0){
                break;
            }
        }
        Collections.shuffle(assignMap.get(workerIndex));
    }

    public Map<Integer, List<Integer>> getAssignMap() {
        return assignMap;
    }

    public void setAssignMap(Map<Integer, List<Integer>> assignMap) {
        this.assignMap = assignMap;
    }

    public int getWorkerNum() {
        return workerNum;
    }

    public void setWorkerNum(int workerNum) {
        this.workerNum = workerNum;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }

    public int[] getP() {
        return p;
    }

    public void setP(int[] p) {
        this.p = p;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public void setDistanceMatrix(double[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    public double[][] getTaskDistanceMatrix() {
        return taskDistanceMatrix;
    }

    public void setTaskDistanceMatrix(double[][] taskDistanceMatrix) {
        this.taskDistanceMatrix = taskDistanceMatrix;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getSurvivalRate() {
        return survivalRate;
    }

    public void setSurvivalRate(double survivalRate) {
        this.survivalRate = survivalRate;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "assignMap=" + assignMap +
                ", workerNum=" + workerNum +
                ", taskNum=" + taskNum +
                ", p=" + Arrays.toString(p) +
                ", q=" + q +
                ", distanceMatrix=" + Arrays.toString(distanceMatrix) +
                ", taskDistanceMatrix=" + Arrays.toString(taskDistanceMatrix) +
                ", distance=" + distance +
                ", fitness=" + fitness +
                ", survivalRate=" + survivalRate +
                '}';
    }
}
