package cn.crowdos.kernel;

import cn.crowdos.kernel.TrustBasedIncentive.TrustBasedIncentive;
import cn.crowdos.kernel.TrustBasedIncentive.TrustBasedIncentiveImpl;
import cn.crowdos.kernel.algorithms.*;
import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.system.DuplicateResourceNameException;
import cn.crowdos.kernel.system.SystemResourceCollection;
import cn.crowdos.kernel.system.resource.*;
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

    /**
     *  If the kernel is not initialized, throw an exception when calling any method except `initial` and `isInitialed`
     *
     * @return A proxy object that wraps the kernel object.
     */
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
    /**
     * This function returns the version of the CrowdOS CrowdKernel.
     *
     * @return The version of the CrowdOS kernel.
     */
    public static String version(){
        return "CrowdOS CrowdKernel v1.0";
    }

    /**
     *  The shutdown function sets the kernel to null
     */
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
            systemResourceCollection.register(new AlgoContainer(new AlgoFactoryAdapter(systemResourceCollection)),"DefaultAlgo");
            systemResourceCollection.register(new Scheduler(systemResourceCollection));
            systemResourceCollection.register(new MissionHistory());
            systemResourceCollection.register(new TrustBasedIncentiveImpl());
            systemResourceCollection.register(new AlgoContainer(new PTMostFactory(systemResourceCollection)),"PTMost");
            systemResourceCollection.register(new AlgoContainer(new T_MostFactory(systemResourceCollection)),"T_Most");
            systemResourceCollection.register(new AlgoContainer(new T_RandomFactory(systemResourceCollection)),"T_Random");
            systemResourceCollection.register(new AlgoContainer(new GGA_IFactory(systemResourceCollection)),"GGA_I");
        } catch (DuplicateResourceNameException e) {
            throw new RuntimeException(e);
        }
        initialed = true;
    }

    //提供默认算法实现DefaultAlgo
    Scheduler Algoresource = systemResourceCollection.getResourceHandler(Scheduler.class,"DefaultAlgo").getResource();
    
    //系统也默认提供PTMost、T_Most、T_Random、GGA_I四种算法，如有需要可自行选择
    @Override
    public void AlgoSelect(String name){
        switch (name) {
            case "PTMost":
                Algoresource = systemResourceCollection.getResourceHandler(Scheduler.class, "PTMost").getResource();
                break;
            case "T_Most":
                Algoresource = systemResourceCollection.getResourceHandler(Scheduler.class, "T_Most").getResource();
                break;
            case "T_Random":
                Algoresource = systemResourceCollection.getResourceHandler(Scheduler.class, "T_Random").getResource();
                break;
            case "GGA_I":
                Algoresource = systemResourceCollection.getResourceHandler(Scheduler.class, "GGA_I").getResource();
                break;
            default:break;
        }
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

        return Algoresource.taskAssignment(task);
    }
    @Override
    public Map<Participant, Double> getTaskIncentiveAssignmentScheme(Task task , Double rewards){

        MissionHistory resource = systemResourceCollection.getResourceHandler(MissionHistory.class).getResource();
        TrustBasedIncentive tbi =  systemResourceCollection.getResourceHandler(TrustBasedIncentive.class).getResource();

        List<Mission> missions = resource.getMissionsByTask(task);
        Mission firstMission = missions.get(0);
        Participant firstSubmitParticipant = firstMission.getFirstSubmitParticipant();
        List<Participant> participants = firstMission.getParticipants();

        tbi.allocateRewards(rewards,task,firstSubmitParticipant,participants);
        return tbi.IncentiveAssignment();
    }

    @Override
    public List<List<Participant>> getTaskAssignmentScheme(ArrayList<Task> tasks){

        return Algoresource.taskAssignment(tasks);
    }

    @Override
    public List<Participant> getTaskRecommendationScheme(Task task){

        return Algoresource.taskRecommendation(task);
    }
    @Override
    public List<List<Participant>> getTaskRecommendationScheme(ArrayList<Task> tasks){

        return Algoresource.taskRecommendation(tasks);
    }
    @Override
    public List<Participant> getTaskParticipantSelectionResult(Task task){

        return Algoresource.participantSelection(task);
    }
    @Override
    public List<List<Participant>> getTaskParticipantSelectionResult(ArrayList<Task> tasks){

        return Algoresource.participantSelection(tasks);
    }
    @Override
    public List<Participant> getParticipants(){
        ParticipantPool resource = systemResourceCollection.getResourceHandler(ParticipantPool.class).getResource();
        return new ArrayList<>(resource);
    }
}
