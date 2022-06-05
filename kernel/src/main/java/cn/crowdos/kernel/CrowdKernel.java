package cn.crowdos.kernel;

import cn.crowdos.kernel.algorithms.TrivialAlgoFactory;
import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.system.DuplicateResourceNameException;
import cn.crowdos.kernel.system.SystemResourceCollection;
import cn.crowdos.kernel.system.SystemResourceHandler;
import cn.crowdos.kernel.system.resource.*;
import cn.crowdos.kernel.tasksystem.Scheduler;
import cn.crowdos.kernel.tasksystem.Task;

import java.util.ArrayList;

public class CrowdKernel {

    private boolean initialed = false;
    private static CrowdKernel kernel;

    private SystemResourceCollection systemResourceCollection;

    public static String version(){
        return "CrowdOS Kernel v1.0";
    }

    private CrowdKernel(){}

    public static CrowdKernel getKernel(){
        if (kernel != null) return kernel;
        kernel = new CrowdKernel();
        return kernel;
    }

    public boolean submit(Task task){
        if (! initialed) throw new UninitializedKernelException();
        return true;
    }

    public void initial(Object...args){
        systemResourceCollection = new SystemResourceCollection();
        try {
            systemResourceCollection.register(new TaskPool());
            systemResourceCollection.register(new ParticipantPool());
            systemResourceCollection.register(new AlgoContainer(new TrivialAlgoFactory(systemResourceCollection)));
            systemResourceCollection.register(new Scheduler(systemResourceCollection));
        } catch (DuplicateResourceNameException e) {
            throw new RuntimeException(e);
        }
        initialed = true;
    }

    public void initial(){
        initial((Object) null);
    }

    public SystemResourceCollection getSystemResourceCollection() {
        return systemResourceCollection;
    }

    public void otherOpera(){
        if (! initialed) throw new UninitializedKernelException();
    }
}
