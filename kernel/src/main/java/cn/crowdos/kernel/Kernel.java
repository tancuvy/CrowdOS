package cn.crowdos.kernel;

import cn.crowdos.kernel.algorithms.TrivialAlgoFactory;
import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.system.DuplicateResourceNameException;
import cn.crowdos.kernel.system.SystemResourceCollection;
import cn.crowdos.kernel.system.resource.AlgoContainer;
import cn.crowdos.kernel.system.resource.ParticipantPool;
import cn.crowdos.kernel.system.resource.TaskPool;
import cn.crowdos.kernel.resource.Task;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class Kernel implements CrowdKernel {

    private boolean initialed = false;
    private static CrowdKernel kernel;

    private SystemResourceCollection systemResourceCollection;

    private Kernel(){}

    public static CrowdKernel getKernel(){
        if (kernel != null) return kernel;
        Kernel crowdKernel = new Kernel();
        kernel = (CrowdKernel) Proxy.newProxyInstance(
                crowdKernel.getClass().getClassLoader(),
                crowdKernel.getClass().getInterfaces(),
                new InvocationHandler() {
                    final Set<String> checkedMethod;
                    {
                        checkedMethod = new HashSet<>();
                        Class<CrowdKernel> crowdKernelClass = CrowdKernel.class;
                        for (Method method : crowdKernelClass.getMethods()) {
                            String name = method.getName();
                            if (!name.equals("initial") && !name.equals("isInitialed")) {
                                checkedMethod.add(name);
                            }
                        }
                    }
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (checkedMethod.contains(method.getName()) && !crowdKernel.isInitialed())
                            throw new UninitializedKernelException();
                        return method.invoke(crowdKernel, args);
                    }
                }
        );
        return kernel;
    }
    public static String version(){
        return "CrowdOS CrowdKernel v1.0";
    }

    public static void shutdown() {
        kernel = null;
    }


    // CrowdKernel APIs

    @Override
    public boolean isInitialed(){
        return initialed;
    }
    @Override
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

    @Override
    public void initial(){
        initial((Object) null);
    }
    @Override
    public SystemResourceCollection getSystemResourceCollection() {
        return systemResourceCollection;
    }
    @Override
    public boolean submitTask(Task task){
        TaskPool resource = systemResourceCollection.getResourceHandler(TaskPool.class).getResource();
        resource.add(task);
        return true;
    }
    @Override
    public boolean registerParticipant(Participant participant) {
        ParticipantPool resource = systemResourceCollection.getResourceHandler(ParticipantPool.class).getResource();
        resource.add(participant);
        return true;
    }
    @Override
    public List<Task> getTasks(){
        TaskPool resource = systemResourceCollection.getResourceHandler(TaskPool.class).getResource();
        return new ArrayList<>(resource);
    }
    @Override
    public List<Participant> getTaskAssignmentScheme(Task task){
        Scheduler resource = systemResourceCollection.getResourceHandler(Scheduler.class).getResource();
        return resource.taskAssignment(task);
    }
    @Override
    public List<Participant> getTaskRecommendationScheme(Task task){
        Scheduler resource = systemResourceCollection.getResourceHandler(Scheduler.class).getResource();
        return resource.taskRecommendation(task);
    }
    @Override
    public List<Participant> getTaskParticipantSelectionResult(Task task){
        Scheduler resource = systemResourceCollection.getResourceHandler(Scheduler.class).getResource();
        return resource.participantSelection(task);
    }
    @Override
    public List<Participant> getParticipants(){
        ParticipantPool resource = systemResourceCollection.getResourceHandler(ParticipantPool.class).getResource();
        return new ArrayList<>(resource);
    }
}
