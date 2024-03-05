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

package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class TemporalIntervalTest {

    TemporalInterval temporalInterval;

    @BeforeEach
    void setUp() throws InvalidConstraintException {
        temporalInterval = new TemporalInterval("10:00", "11:00");
    }

    @Test
    void testCreate() {
        assertThrows(InvalidConstraintException.class, () -> {new TemporalInterval("11:00:00", "09:00:00");});
    }

    @Test
    void decomposer() throws DecomposeException {
        Decomposer<Constraint> decomposer = temporalInterval.decomposer();
        System.out.println(decomposer.trivialDecompose());
        System.out.println(decomposer.scaleDecompose(4));
    }

    @Test
    void satisfy() throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("hh:mm");
        DateCondition time1;
        DateCondition time2;
        time1 = new DateCondition(sf.parse("10:30").getTime());
        time2 = new DateCondition(sf.parse("11:30").getTime());
        assertTrue(temporalInterval.satisfy(time1));
        assertFalse(temporalInterval.satisfy(time2));
    }

    @Test
    void getConditionClass() {
        assertSame(temporalInterval.getConditionClass(), DateCondition.class);
    }
}