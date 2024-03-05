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
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SpatioPolygonTest{
    SpatioPolygon spatioPolygon;
    {
        List<Coordinate>polygon = new ArrayList<>();
        polygon.add(new Coordinate(0,1));
        polygon.add(new Coordinate(1,3));
        polygon.add(new Coordinate(2,4));
        polygon.add(new Coordinate(4,2));
        polygon.add(new Coordinate(2,0));
        try{
            spatioPolygon = new SpatioPolygon(polygon);
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testException(){
        List<Coordinate>polygon1 = new ArrayList<>();
        polygon1.add(new Coordinate(0,1));
        polygon1.add(new Coordinate(1,3));
        assertThrows(
                InvalidConstraintException.class,
                ()->new SpatioPolygon(polygon1)
        );

        List<Coordinate>polygon2 = new ArrayList<>();
        polygon2.add(new Coordinate(0,1));
        polygon2.add(new Coordinate(1,3));
        polygon2.add(new Coordinate(2,4));
        polygon2.add(new Coordinate(2,0));
        polygon2.add(new Coordinate(4,2));
        assertThrows(
                InvalidConstraintException.class,
                ()->new SpatioPolygon(polygon2)
        );
    }

    @Test
    void satisfy(){
        Coordinate coordinate1 = new Coordinate(2,2);
        Coordinate coordinate2 = new Coordinate(4,0);
        assertTrue(spatioPolygon.satisfy(coordinate1));
        assertFalse(spatioPolygon.satisfy(coordinate2));
    }

    @Test
    void decomposer(){
        Decomposer<Constraint> decomposer = spatioPolygon.decomposer();
        try{
            for (Constraint subconstraint : decomposer.scaleDecompose(15)){
                System.out.println(subconstraint);
            }
        } catch (DecomposeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getConditionClass(){
        String name = spatioPolygon.getConditionClass().getName();
        assertEquals(name, Coordinate.class.getName());
    }
}


