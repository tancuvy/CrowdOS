package cn.crowdos.kernel.system.resource;

import cn.crowdos.kernel.algorithms.AlgoFactory;
import cn.crowdos.kernel.system.SystemResourceHandler;

public class AlgoSet implements Resource<AlgoFactory>{

    private final AlgoFactory factory;
    public AlgoSet(AlgoFactory factory){
        this.factory = factory;
    }

    @Override
    public SystemResourceHandler<AlgoFactory> getHandler() {
        return new SystemResourceHandler<AlgoFactory>(factory) {
            @Override
            public AlgoFactory getResourceView() {
                return source;
            }

            @Override
            public AlgoFactory getResource() {
                return source;
            }
        };
    }
}
