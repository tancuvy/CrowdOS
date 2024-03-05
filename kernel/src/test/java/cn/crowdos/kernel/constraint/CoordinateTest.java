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

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

    @Test
    void inLine() {
        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(0, 0);
        Coordinate c3 = new Coordinate(0, 1);
        Coordinate c4 = new Coordinate(1, 0);
        Coordinate c5 = new Coordinate(1, 1);
        assertTrue(c1.inLine(c2));
        assertTrue(c1.inLine(c3));
        assertTrue(c1.inLine(c4));
        assertFalse(c1.inLine(c5));
    }

    @Test
    void testToString() {
        Coordinate c1 = new Coordinate(0, 0);
        System.out.println(c1);
    }
}