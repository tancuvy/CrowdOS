package cn.crowdos.kernel;

public class CrowdKernel {

    private boolean initialed = false;

    private static CrowdKernel kernel;
    public static String version(){
        return "CrowdOS cn.crowdos.kernel.Kernel v1.0";
    }

    private CrowdKernel(){}

    public static CrowdKernel getKernel(){
        if (kernel != null) return kernel;
        kernel = new CrowdKernel();
        return kernel;
    }

    public void initial(Object...args){
        initialed = true;
    }

    public void otherOpera(){
        if (! initialed) throw new UnsupportedOperationException();
    }
}
