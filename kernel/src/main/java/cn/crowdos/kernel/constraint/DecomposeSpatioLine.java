package cn.crowdos.kernel.constraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DecomposeSpatioLine {
    private final List<Coordinate>pointsList;
    private final double width;

    public DecomposeSpatioLine(double width,List<Coordinate> pointsList) throws InvalidConstraintException {
        this.pointsList = pointsList;
        this.width = width;
        DecomposePointslistToLines(pointsList);
    }

    public DecomposeSpatioLine(double width,Coordinate...pointsList) throws InvalidConstraintException {
        this(width,Arrays.asList(pointsList));
        DecomposePointslistToLines(Arrays.asList(pointsList));
    }

    private void DecomposePointslistToLines(List<Coordinate> pointsList) throws InvalidConstraintException {
        for(int i=0;i<pointsList.size()-1;i++){
            Coordinate startPoint = pointsList.get(i);
            Coordinate endPoint = pointsList.get(i+1);
            SpatioLine spatioLine = new SpatioLine(startPoint,endPoint,width);
//            System.out.println(spatioLine.description());
        }
    }

//    public static void main(String []args) throws InvalidConstraintException {
//        List<Coordinate>pointsList = new ArrayList<>();
//        pointsList.add(new Coordinate(0,1));
//        pointsList.add(new Coordinate(1,3));
//        pointsList.add(new Coordinate(2,4));
//        pointsList.add(new Coordinate(4,2));
//        pointsList.add(new Coordinate(2,0));
//        DecomposeSpatioLine decomposeSpatioLine = new DecomposeSpatioLine(2,pointsList);
//    }
}
