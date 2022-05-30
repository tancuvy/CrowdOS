package cn.crowdos.kernel.constraint;

public class Coordinate implements Condition{
    final double longitude;
    final double latitude;
    public Coordinate(double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Coordinate(){
        this(0,0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Coordinate){
            Coordinate anCoord = (Coordinate) obj;
            return this.longitude == anCoord.longitude && this.latitude == anCoord.latitude;
        }
        return false;
    }

    public boolean inLine(Coordinate other){
        return this.longitude == other.longitude || this.latitude == other.latitude;
    }
}
