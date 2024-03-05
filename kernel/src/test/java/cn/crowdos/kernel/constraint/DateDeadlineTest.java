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
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DateDeadlineTest {

    DateDeadline dateDeadline;
    {
        try{
            dateDeadline = new DateDeadline("2023-04-18");
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testException(){
        assertThrows(
                InvalidConstraintException.class,
                () -> new DateDeadline("2022-04-07")
        );
        assertThrows(
                InvalidConstraintException.class,
                () -> new DateDeadline(LocalDate.of(2022,4,7))
        );
    }

    @Test
    void decompser(){
        Decomposer<Constraint> decomposer = dateDeadline.decomposer();
        try{
            for (Constraint subconstraint : decomposer.scaleDecompose(10)){
                System.out.println(subconstraint);
            }
        } catch (DecomposeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void satisfy() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        DateCondition condition1, condition2;
        try {
            condition1 = new DateCondition(df.parse("2023.04.10").getTime());
            condition2 = new DateCondition(df.parse("2023.06.04").getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(dateDeadline.satisfy(condition1));
        assertFalse(dateDeadline.satisfy(condition2));
    }

    @Test
    void getConditionClass(){
        assertSame(dateDeadline.getConditionClass(), DateCondition.class);
    }
}
