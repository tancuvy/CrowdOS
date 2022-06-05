package cn.crowdos.kernel.system;

import java.util.Collections;
import java.util.List;

public abstract class SystemResourceHandler<T> {
    protected T source;
    public SystemResourceHandler(T source){
        this.source = source;
    }
    public abstract T getResourceView();
    public abstract T getResource();
}
