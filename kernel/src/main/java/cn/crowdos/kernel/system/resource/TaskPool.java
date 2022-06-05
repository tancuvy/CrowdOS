package cn.crowdos.kernel.system.resource;

import cn.crowdos.kernel.system.SystemResourceHandler;
import cn.crowdos.kernel.tasksystem.Task;

import java.util.LinkedList;

public class TaskPool extends LinkedList<Task> implements Resource<TaskPool> {
    @Override
    public SystemResourceHandler<TaskPool> getHandler() {
        return new SystemResourceHandler<TaskPool>(this) {

            @Override
            public TaskPool getResourceView() {
                return source;
            }

            @Override
            public TaskPool getResource() {
                return source;
            }
        };
    }
}
