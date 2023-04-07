package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;

import java.util.Collections;
import java.util.List;

public class SpatioPoint implements Constraint{
    private final Coordinate center;
    private final double radius;

    public SpatioPoint(Coordinate center, double radius) throws InvalidConstraintException{
        this.center = center;
        this.radius = radius;
        if(radius<=0){
            throw new InvalidConstraintException(String.format("radius %s is invalid",this.radius));
        }
    }

    @Override
    public boolean satisfy(Condition condition) {
        if(!(condition instanceof Coordinate)) return false;
        Coordinate coordinate = (Coordinate) condition;
        return ComputePointToPointDistance(coordinate,center)<=radius;
    }

    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                try{
                    return Collections.singletonList(new SpatioPoint(center,radius));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public List<Constraint> scaleDecompose(int scale) throws DecomposeException {
                return this.trivialDecompose();
            }
        };
    }

    @Override
    public Class<? extends Condition> getConditionClass() {
        return Coordinate.class;
    }

    @Override
    public String toString(){
        return "SpatioPoint{" +
                "Center=" + center +
                "radius=" + radius +
                "}";
    }

    @Override
    public String description() {
        return toString();
    }


    private double ComputePointToPointDistance(Coordinate point1,Coordinate point2){
        double longitudeDifference = point1.longitude - point2.longitude;
        double latitudeDifference = point1.latitude - point2.latitude;
        return Math.sqrt(longitudeDifference*longitudeDifference+latitudeDifference*latitudeDifference);
    }

    /*
    public static void main(String []args) throws InvalidConstraintException {
        SpatioPoint spatioPoint = new SpatioPoint(new Coordinate(0,0),5);
        System.out.println(spatioPoint.satisfy(new Coordinate(2,0)));
        System.out.println(spatioPoint.satisfy(new Coordinate(6,0)));
        System.out.println(spatioPoint.description());
    }
    */
}
