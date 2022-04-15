package cn.crowdos.kernel.resource;

public interface Resource {
    boolean available();
    ResourceStatus getCurrentStatus();
}
