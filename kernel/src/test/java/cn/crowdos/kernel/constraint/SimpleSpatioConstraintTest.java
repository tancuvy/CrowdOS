package cn.crowdos.kernel.constraint;

import javafx.scene.layout.CornerRadii;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class SimpleSpatioConstraintTest {

    SimpleSpatioConstraint constraint;
    {
        Coordinate leftTop = new Coordinate(0, 0);
        Coordinate rightBottom = new Coordinate(10, 10);
        try {
            constraint = new SimpleSpatioConstraint(leftTop, rightBottom);
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    @Test()
    void newException(){
        assertThrows(
                InvalidConstraintException.class,
                () -> new SimpleSpatioConstraint(new Coordinate(0, 0), new Coordinate(0, 100))
        );
    }

    @Test
    void satisfy() {
        Coordinate c1 = new Coordinate(0, 0);
        assertTrue(constraint.satisfy(c1));
        Coordinate c2 = new Coordinate(10, 10);
        assertFalse(constraint.satisfy(c2));
        Coordinate c3 = new Coordinate(5, 5);
        assertTrue(constraint.satisfy(c3));
        Coordinate c4 = new Coordinate(20, 20);
        assertFalse(constraint.satisfy(c4));
    }

    @Test
    @DisplayName("123")
    void getConditionClass() {
        String name = constraint.getConditionClass().getName();
        assertEquals(name, Coordinate.class.getName());
    }

    @Test
    void decomposer() {
    }
}