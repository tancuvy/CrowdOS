package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.Decomposer;

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
    public boolean satisfy(Condition condition) {
        if (!(condition instanceof Coordinate)) return false;
        Coordinate coord = (Coordinate) condition;
        return range[0].longitude <= coord.longitude && range[0].latitude <= coord.latitude
                && coord.longitude < range[1].longitude && coord.latitude < range[0].latitude;
    }

    @Override
    public Class<? extends Condition> getConditionClass() {
        return Coordinate.class;
    }

    @Override
    public String description() {
        return toString();
    }

    @Override
    public Decomposer<Constraint> decomposer() {
        return null;
    }
}
