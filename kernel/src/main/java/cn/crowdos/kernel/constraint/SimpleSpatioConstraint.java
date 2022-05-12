package cn.crowdos.kernel.constraint;

import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.CornerRadii;

public class SimpleSpatioConstraint implements Constraint {

    Coordinate[] range;

    public SimpleSpatioConstraint(Coordinate topLeft, Coordinate bottomRight) throws InvalidConstraintException {
        if (topLeft.inLine(bottomRight)) throw new InvalidConstraintException();
        this.range = new Coordinate[]{topLeft, bottomRight};
    }

    public SimpleSpatioConstraint(Coordinate bottomRight) throws InvalidConstraintException {
        Coordinate topLeft = new Coordinate();
        if (topLeft.inLine(bottomRight)) throw new InvalidConstraintException();
        this.range = new Coordinate[]{topLeft, bottomRight};
    }

    @Override
    public boolean satisfy(Object condition) {
        if (!(condition instanceof Coordinate)) return false;
        Coordinate coord = (Coordinate) condition;
        return range[0].longitude <= coord.longitude && range[0].latitude <= coord.latitude
                && coord.longitude < range[1].longitude && coord.latitude < range[0].latitude;
    }

    @Override
    public String description() {
        return toString();
    }
}
