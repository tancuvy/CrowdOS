package cn.crowdos.kernel;

public class Kernel {
    public static String version(){
        return "CrowdOS cn.crowdos.kernel.Kernel v1.0";
    }

    public static void main(String[] args) {
        System.out.println(Kernel.version());
    }
}
