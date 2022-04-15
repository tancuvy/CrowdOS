package cn.crowdos.kernel.constraint;


public abstract class SpatialConstraint extends Constraint {


}

class Coordinate {
    public final double longitude;
    public final double latitude;

    public Coordinate(double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
