package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import javafx.scene.layout.CornerRadii;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpatioPolygon implements Constraint{
    private final List<Coordinate> polygon;

    public SpatioPolygon(List<Coordinate> polygon) throws InvalidConstraintException{
        this.polygon = polygon;
        if(!PolygonValidityChecker(polygon)){
            throw new InvalidConstraintException(String.format("polygon is invalid"));
        }
    }

    public SpatioPolygon(Coordinate... polygon) throws InvalidConstraintException {
        this(Arrays.asList(polygon));
    }

    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                try {
                    return Collections.singletonList(new SpatioPolygon(polygon));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public List<Constraint> scaleDecompose(int scale) throws DecomposeException {
                if(scale<0) throw new DecomposeException("invalid decompose scale");
                if(scale==1) return trivialDecompose();
                List<Constraint> subConstraints = new ArrayList<>();
                try {
                     subConstraints = ComputeSubPolygon(polygon,(int) Math.sqrt(scale));
                } catch (InvalidConstraintException e) {
                    throw new RuntimeException(e);
                }
                return subConstraints;
            }
        };
    }

    @Override
    public boolean satisfy(Condition condition) {
        if(!(condition instanceof Coordinate)) return false;
        Coordinate coordinate = (Coordinate) condition;
        return PointInPolygon(coordinate,polygon);
    }

    @Override
    public Class<? extends Condition> getConditionClass() {
        return Coordinate.class;
    }

    @Override
    public String toString(){
        String str = "SpatioPolygon{";
        for(Object item:polygon){
            str += item;
            str += ";";
        }
        str += "}";
        return str;
    }

    @Override
    public String description() {
        return toString();
    }

    private boolean PolygonValidityChecker (List<Coordinate> polygon){
        int pointsNum = polygon.size();
        if(pointsNum<3) return false;

        for(int i=0;i<polygon.size();i++){
            Coordinate p1 = polygon.get(i);
            Coordinate p2 = polygon.get((i+1)%pointsNum);
            if(p1.equals(p2)) return false;
        }

        for (int i = 0; i < pointsNum; i++) {
            Coordinate A = polygon.get(i);
            Coordinate B = polygon.get((i + 1) % pointsNum);
            for (int j = i + 2; j < pointsNum; j++) {
                Coordinate C = polygon.get(j);
                Coordinate D = polygon.get((j + 1) % pointsNum);
                if (SegmentsIntersect(A, B, C, D)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean SegmentsIntersect(Coordinate A, Coordinate B, Coordinate C, Coordinate D) {
        double ccw1 = ccw(A, B, C);
        double ccw2 = ccw(A, B, D);
        double ccw3 = ccw(C, D, A);
        double ccw4 = ccw(C, D, B);
        return (ccw1 * ccw2 < 0) && (ccw3 * ccw4 < 0);
    }

    private double ccw(Coordinate A, Coordinate B, Coordinate C) {
        return (B.longitude - A.longitude) * (C.latitude - A.latitude) - (B.latitude - A.latitude) * (C.longitude - A.longitude);
    }


    public static boolean PointInPolygon(Coordinate point, List<Coordinate> polygon) {
        int crossings = 0;
        for (int i = 0; i < polygon.size(); i++) {
            Coordinate vertex1 = polygon.get(i);
            Coordinate vertex2 = polygon.get((i + 1) % polygon.size());

            if (vertex1.longitude== vertex2.longitude) {
                continue;
            }

            if (point.longitude < Math.min(vertex1.longitude, vertex2.longitude)) {
                continue;
            }

            if (point.longitude >= Math.max(vertex1.longitude, vertex2.longitude)) {
                continue;
            }

            double slope = (vertex2.latitude - vertex1.latitude) / (vertex2.longitude - vertex1.longitude);
            double intersect = vertex1.latitude + (point.longitude - vertex1.longitude) * slope;

            if (intersect > point.latitude) {
                crossings++;
            }
        }

        return (crossings % 2 != 0);
    }

    // 划分多边形子区域
    public List<Constraint> ComputeSubPolygon(List<Coordinate> polygon, int copies) throws InvalidConstraintException {
        List<Constraint>subPolygons = new ArrayList<>();
        // 找到多边形的边界
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
        for (Coordinate point : polygon) {
            if (point.longitude < minX) minX = point.longitude;
            if (point.longitude > maxX) maxX = point.longitude;
            if (point.latitude < minY) minY = point.latitude;
            if (point.latitude > maxY) maxY = point.latitude;
        }

        double xstep = (maxX - minX)/copies;
        double ystep = (maxY - minY)/copies;

        // 计算矩形框的四个角点
        Coordinate topLeft = new Coordinate(minX, maxY);
        Coordinate topRight = new Coordinate(maxX, maxY);
        Coordinate bottomLeft = new Coordinate(minX, minY);
        Coordinate bottomRight = new Coordinate(maxX, minY);
        // 计算矩形框内的小格子左上角坐标和右下角坐标，并保存到列中
        List<Coordinate[]> cells = new ArrayList<>();
        for (double x = minX; x < maxX; x += xstep) {
            for (double y = minY; y < maxY; y += ystep) {
                Coordinate cellTopLeft = new Coordinate(x, y);
                Coordinate cellBottomRight = new Coordinate(x + xstep, y - ystep);
                cells.add(new Coordinate[]{cellTopLeft, cellBottomRight});
            }
        }
        // 判断每个小格子是否在多边形内部，并保存在新列表中
        List<Coordinate> result = new ArrayList<>();
        for (Coordinate[] cell : cells) {
            Coordinate cellCenter = new Coordinate(cell[0].longitude + xstep / 2, cell[1].latitude + ystep / 2);
            if (PointInPolygon(cellCenter,polygon)) {
                result.add(cell[0]); // 左上角
                result.add(new Coordinate(cell[0].longitude, cell[1].latitude)); // 左下角
                result.add(cell[1]); // 右下角
                result.add(new Coordinate(cell[1].longitude, cell[0].latitude)); // 右上角
            }
        }
        for(int i=0;i<result.size();i++){
            Coordinate firstPoint = result.get(i);
            Coordinate secondPoint = new Coordinate(firstPoint.longitude,firstPoint.latitude+ystep);
            Coordinate thirdPoint = new Coordinate(firstPoint.longitude+xstep,firstPoint.latitude+ystep);
            SpatioPolygon subPolygon = new SpatioPolygon(firstPoint,secondPoint,thirdPoint);
            subPolygons.add(subPolygon);
        }
        return subPolygons;
    }

    private boolean CellInPolygon(List<Coordinate> polygon, Coordinate point) {
        boolean result = false;
        int j = polygon.size() - 1;
        for (int i = 0; i < polygon.size(); i++) {
            if ((polygon.get(i).latitude < point.latitude && polygon.get(j).latitude >= point.latitude
                    || polygon.get(j).latitude < point.latitude && polygon.get(i).latitude >= point.latitude)
                    && (polygon.get(i).longitude + (point.latitude - polygon.get(i).latitude)
                    / (polygon.get(j).latitude - polygon.get(i).latitude)
                    * (polygon.get(j).longitude - polygon.get(i).longitude) < point.longitude)) {
                result = !result;
            }
            j = i;
        }
        return result;
    }

//    public static void main(String []args) throws InvalidConstraintException {
//        List<Coordinate> polygon = new ArrayList<>();
//        polygon.add(new Coordinate(0,1));
//        polygon.add(new Coordinate(1,3));
//        polygon.add(new Coordinate(2,4));
//        polygon.add(new Coordinate(4,2));
//        polygon.add(new Coordinate(2,0));
//        SpatioPolygon spatioPolygon = new SpatioPolygon(polygon);
//        System.out.println(spatioPolygon.satisfy(new Coordinate(2,2)));
//        System.out.println(spatioPolygon.satisfy(new Coordinate(4,4)));
//        System.out.println(spatioPolygon.description());
//    }
}

