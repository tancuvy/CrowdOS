package cn.crowdos.kernel.system;

import cn.crowdos.kernel.algorithms.AlgoFactory;
import cn.crowdos.kernel.algorithms.TrivialAlgoFactory;
import cn.crowdos.kernel.system.resource.AlgoSet;
import cn.crowdos.kernel.system.resource.Resource;
import cn.crowdos.kernel.system.resource.TaskPool;

import java.util.HashMap;
import java.util.Map;

public class SystemResourceCollection {
    Map<Class<?>, Map<String, Resource<?>>> resourceMap;

    public SystemResourceCollection(){
        resourceMap = new HashMap<>();
    }
    public void register(Resource<?> resource) throws DuplicateResourceNameException {
        register(resource, "default");
    }
    public void register(Resource<?> resource, String resourceName) throws DuplicateResourceNameException {
        if (!resourceMap.containsKey(resource.getClass())) {
            resourceMap.put(resource.getClass(), new HashMap<>());
        }
        Map<String, Resource<?>> resList = resourceMap.get(resource.getClass());
        if (resList.size() != 0 && resList.containsKey(resourceName)) {
            throw new DuplicateResourceNameException();
        }
        resList.put(resourceName, resource);
    }

    public <T> SystemResourceHandler<T> getResourceHandler(Class<? extends Resource<T>> resourceClass, String resourceName){
        Map<String, Resource<?>> stringResourceMap = resourceMap.get(resourceClass);
        if (stringResourceMap != null) {
            Resource<?> resource = stringResourceMap.get(resourceName);
            if (resource != null){
                return resourceClass.cast(resource).getHandler();
            }
        }
        return null;
    }


    public <T> SystemResourceHandler<T> getResourceHandler(Class<? extends Resource<T>> resourceClass){
        return getResourceHandler(resourceClass, "default");
    }

    public static void main(String[] args) throws DuplicateResourceNameException {
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
