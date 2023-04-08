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
    }

    public DecomposeSpatioLine(double width,Coordinate...pointsList) throws InvalidConstraintException {
        this(width,Arrays.asList(pointsList));
    }

    public List<SpatioLine> DecomposePointslistToLines() throws InvalidConstraintException {
        List<SpatioLine> decomposeLines = new ArrayList<>(pointsList.size()-1);
        for(int i=0;i<pointsList.size()-1;i++){
            Coordinate startPoint = pointsList.get(i);
            Coordinate endPoint = pointsList.get(i+1);
            SpatioLine spatioLine = new SpatioLine(startPoint,endPoint,width);
            decomposeLines.add(spatioLine);
        }
        return decomposeLines;
    }
}
