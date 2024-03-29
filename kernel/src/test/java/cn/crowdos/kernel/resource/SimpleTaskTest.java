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

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.common.TimeParticipant;
import cn.crowdos.kernel.constraint.InvalidConstraintException;
import cn.crowdos.kernel.constraint.SimpleTimeConstraint;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SimpleTaskTest {
    Task task;
    {
        SimpleTimeConstraint timeConst;
        try {
            timeConst = new SimpleTimeConstraint("2022.6.1", "2022.6.5");
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
        task = new SimpleTask(Collections.singletonList(timeConst), Task.TaskDistributionType.RECOMMENDATION);
    }

    @Test
    void decomposer() {
        Decomposer<Task> decomposer = task.decomposer();
        try {
            for (Task sub : decomposer.decompose(5)) {
                System.out.println(sub);
            }
        } catch (DecomposeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getTaskDistributionType() {
        System.out.println(task.getTaskDistributionType());
    }

    @Test
    void getTaskStatus() {
        System.out.println(task.getTaskStatus());
    }

    @Test
    void constraints() {
        System.out.println(task.constraints());
    }

    @Test
    void canAssignTo() {

        TimeParticipant p1 = new TimeParticipant("2022.6.3");
        TimeParticipant p2 = new TimeParticipant("2022.6.10");
        assertTrue(task.canAssignTo(p1));
        assertFalse(task.canAssignTo(p2));
    }

    @Test
    void assignable() {
        System.out.println(task.assignable());
    }

    @Test
    void finished() {
        System.out.println(task.finished());
    }
}

