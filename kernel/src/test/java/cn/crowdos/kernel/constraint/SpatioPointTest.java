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

import cn.crowdos.kernel.wrapper.DateCondition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpatioPointTest {
    SpatioPoint spatioPoint;
    {
        try{
            spatioPoint = new SpatioPoint(new Coordinate(55.764,59.253),10);
        }catch (InvalidConstraintException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void testException(){
        assertThrows(
                InvalidConstraintException.class,
                ()->new SpatioPoint(new Coordinate(55.764,69.253),-10)
        );
    }

    @Test
    void satisfy(){
        Coordinate coordinate1 = new Coordinate(57.563,60.125);
        assertTrue(spatioPoint.satisfy(coordinate1));
        Coordinate coordinate2 = new Coordinate(12.589,0.265);
        assertFalse(spatioPoint.satisfy(coordinate2));
    }

    @Test
    void getConditionClass(){
        String name = spatioPoint.getConditionClass().getName();
        assertEquals(name, Coordinate.class.getName());
    }
}
