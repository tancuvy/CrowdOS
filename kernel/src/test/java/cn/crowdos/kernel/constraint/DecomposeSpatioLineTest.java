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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DecomposeSpatioLineTest {
    DecomposeSpatioLine decomposeSpatioLine;
    double width = 8;
    {
        List<Coordinate>pointList = new ArrayList<>();
        pointList.add(new Coordinate(0,1));
        pointList.add(new Coordinate(1,3));
        pointList.add(new Coordinate(2,4));
        pointList.add(new Coordinate(4,2));
        pointList.add(new Coordinate(2,0));
        try{
            decomposeSpatioLine = new DecomposeSpatioLine(width,pointList);
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testException(){
        List<Coordinate>pointList = new ArrayList<>();
        pointList.add(new Coordinate(0,1));
        pointList.add(new Coordinate(1,3));
        pointList.add(new Coordinate(2,4));
        assertThrows(
                InvalidConstraintException.class,
                () -> new DecomposeSpatioLine(-12,pointList)
        );
    }

    @Test
    void DecomposePointslistToLines() throws InvalidConstraintException {
        List<SpatioLine> decomposeLines = decomposeSpatioLine.DecomposePointslistToLines();
        for(SpatioLine line:decomposeLines){
            System.out.println(line);
        }
    }
}
