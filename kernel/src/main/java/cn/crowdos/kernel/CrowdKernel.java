package cn.crowdos.kernel;

import cn.crowdos.kernel.algorithms.TrivialAlgoFactory;
import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.system.SystemResourceCollection;
import cn.crowdos.kernel.system.SystemResourceHandler;
import cn.crowdos.kernel.system.resource.ResourceItem;
import cn.crowdos.kernel.tasksystem.Scheduler;
import cn.crowdos.kernel.tasksystem.Task;

import java.util.ArrayList;

public class CrowdKernel {

    private boolean initialed = false;
    private static CrowdKernel kernel;
    private SystemResourceCollection systemResourceCollection;
    private Scheduler scheduler;

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
        scheduler = new Scheduler(systemResourceCollection);
        initialed = true;
    }

    public void initial(){
        initial((Object) null);
    }

    public void otherOpera(){
        if (! initialed) throw new UninitializedKernelException();
    }
}
