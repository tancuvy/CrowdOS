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

package cn.crowdos.kernel.resource;

import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.constraint.Condition;
import cn.crowdos.kernel.constraint.Constraint;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractTask implements Task{

    protected final long taskId;
    private static long taskCounter = 1;
    protected final List<Constraint> constraints;
    protected final TaskDistributionType taskDistributionType;
    protected TaskStatus status;

    protected AbstractTask(List<Constraint> constraints, TaskDistributionType taskDistributionType) {
        this.taskId = taskCounter++;
        this.constraints = constraints;
        this.taskDistributionType = taskDistributionType;
    }


    abstract public Decomposer<Task> decomposer();

    public long getTaskId() {
        return this.taskId;
    }

    @Override
    public TaskDistributionType getTaskDistributionType() {
        return taskDistributionType;
    }

    @Override
    public TaskStatus getTaskStatus() {
        return status;
    }

    @Override
    public void setTaskStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public List<Constraint> constraints() {
        return constraints;
    }

    @Override
    public boolean canAssignTo(Participant participant) {
        for (Constraint constraint : constraints) {
            Class<? extends Condition> conditionClass = constraint.getConditionClass();
            if(participant.hasAbility(conditionClass)){
                if (!constraint.satisfy(participant.getAbility(conditionClass)))
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean assignable() {
        return status == TaskStatus.READY;
    }

    @Override
    public boolean finished() {
        return status == TaskStatus.FINISHED;
    }

}
