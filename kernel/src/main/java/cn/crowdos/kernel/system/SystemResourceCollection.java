package cn.crowdos.kernel.system;

import cn.crowdos.kernel.algorithms.AlgoFactory;
import cn.crowdos.kernel.algorithms.TrivialAlgoFactory;
import cn.crowdos.kernel.system.resource.AlgoSet;
import cn.crowdos.kernel.system.resource.Resource;
import cn.crowdos.kernel.system.resource.TaskPool;

import java.util.HashMap;
import java.util.Map;

public class SystemResourceCollection {
    Map<Class<?>, Resource<?>> resourceMap;

    public SystemResourceCollection(){
        resourceMap = new HashMap<>();
    }
    public void register(Resource<?> resource){
        resourceMap.put(resource.getClass(), resource);
    }

    public <T> SystemResourceHandler<T> getResourceHandler(Class<? extends Resource<T>> resourceClass){
        Resource<?> resource = resourceMap.get(resourceClass);
        return resourceClass.cast(resource).getHandler();
    }

    public static void main(String[] args) {
        SystemResourceCollection sc = new SystemResourceCollection();
        sc.register(new TaskPool());
        sc.register(new AlgoSet(new TrivialAlgoFactory(null)));
//        SystemResourceHandler<TaskPool> resourceHandler = sc.getResourceHandler(TaskPool.class);
//        System.out.println(resourceHandler.getResource());
        SystemResourceHandler<AlgoFactory> resourceHandler = sc.getResourceHandler(AlgoSet.class);
        AlgoFactory resource = resourceHandler.getResource();
        resource.getTaskAssignmentAlgo();
    }
}
