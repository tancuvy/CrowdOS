package cn.crowdos.kernel.resource;

public abstract class Participant implements Resource{


    @Override
    public boolean available() {
        return false;
    }
}
