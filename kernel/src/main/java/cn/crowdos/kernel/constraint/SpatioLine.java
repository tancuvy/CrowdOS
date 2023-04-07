package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpatioLine implements Constraint{
    private final Coordinate startPoint;
    private final Coordinate endPoint;
    private final double width;

    public SpatioLine(Coordinate startPoint, Coordinate endPoint, double width) throws InvalidConstraintException{
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.width = width;
        if(width<=0){
            throw new InvalidConstraintException(String.format("width %s is invalid",this.width));
        }
    }

    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                try{
                    return Collections.singletonList(new SpatioLine(startPoint,endPoint,width));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public List<Constraint> scaleDecompose(int scale) throws DecomposeException {
                if(scale<0) throw new DecomposeException("invalid decompose scale");
                if(scale==1) return trivialDecompose();
                double totalDistance = ComputePointToPointDistance(startPoint,endPoint);
                double distancePerIntervel = totalDistance/scale;
                List<Constraint> subConstraints = new ArrayList<>(scale);
                for(int i=0;i<scale;i++){
                    Coordinate intervelStartPoint = ComputePoint(i*distancePerIntervel);
                    Coordinate intervelEndPoint = ComputePoint((i+1)*distancePerIntervel);
                    try{
                        subConstraints.add(new SpatioLine(intervelStartPoint,intervelEndPoint,width));

                    } catch (InvalidConstraintException e) {
                        throw new RuntimeException(e);
                    }
                }
                return subConstraints;
            }
        };
    }

    @Override
    public boolean satisfy(Condition condition) {
        if(!(condition instanceof Coordinate)) return false;
        Coordinate coordinate = (Coordinate) condition;
        return ComputePointToLineDistance(startPoint,endPoint,coordinate)<=width/2;
    }

    @Override
    public Class<? extends Condition> getConditionClass() {
        return Coordinate.class;
    }

    @Override
    public String toString(){
        return "SpatioLine{" +
                "startPoint=" + startPoint +
                "endPoint=" + endPoint +
                "width=" + width +
                "}";
    }

    @Override
    public String description() {
        return toString();
    }

    private double ComputePointToLineDistance(Coordinate start,Coordinate end,Coordinate point){
        double x = point.longitude, y = point.latitude;
        double x1 = start.longitude, y1 = start.latitude;
        double x2 = end.longitude, y2 = end.latitude;
        double cross = (x2 - x1) * (x - x1) + (y2 - y1) * (y - y1);
        if (cross <= 0) {
            return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
        }
        double d2 = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        if (cross >= d2) {
            return Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
        }
        double r = cross / d2;
        double px = x1 + (x2 - x1) * r;
        double py = y1 + (y2 - y1) * r;
        return Math.sqrt((x - px) * (x - px) + (y - py) * (y - py));
    }

    private double ComputePointToPointDistance(Coordinate point1,Coordinate point2){
        double longitudeDifference = point1.longitude - point2.longitude;
        double latitudeDifference = point1.latitude - point2.latitude;
        return Math.sqrt(longitudeDifference*longitudeDifference+latitudeDifference*latitudeDifference);
    }

    private Coordinate ComputePoint(double distance){
        if(endPoint.latitude==startPoint.latitude){
            if(endPoint.latitude>startPoint.latitude) return new Coordinate(startPoint.longitude+distance,startPoint.latitude);
            else return new Coordinate(startPoint.longitude-distance,startPoint.latitude);
        }
        else{
            double k = (endPoint.latitude-startPoint.latitude)/(endPoint.longitude-startPoint.longitude);
            double newLongitude = startPoint.longitude + (distance/Math.sqrt(1+k*k));
            double newLatitude = startPoint.latitude + k*(newLongitude-startPoint.longitude);
            return new Coordinate(newLongitude,newLatitude);
         }
    }

    /*
    public static void main(String []args) throws InvalidConstraintException {
        SpatioLine spatioLine = new SpatioLine(new Coordinate(1,3),new Coordinate(3,1),1);
        System.out.println(spatioLine.satisfy(new Coordinate(3,1.2)));
        System.out.println(spatioLine.satisfy(new Coordinate(3,0)));
        System.out.println(spatioLine.description());
    }
    */
}
