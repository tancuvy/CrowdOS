package cn.crowdos.kernel.system.resource;

import cn.crowdos.kernel.system.SystemResourceHandler;

public abstract class ResourceItem<T> implements Resource<T>{

    protected T resource;
    public ResourceItem(T resource){
        this.resource = resource;
    }
    public abstract SystemResourceHandler<T> getHandler();
}
