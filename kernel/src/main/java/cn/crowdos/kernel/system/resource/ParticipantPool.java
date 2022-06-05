package cn.crowdos.kernel.system.resource;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.system.SystemResourceHandler;

import java.util.LinkedList;

public class ParticipantPool extends LinkedList<Participant> implements Resource<ParticipantPool> {
    @Override
    public SystemResourceHandler<ParticipantPool> getHandler() {
        return new SystemResourceHandler<ParticipantPool>(this) {

            @Override
            public ParticipantPool getResourceView() {
                return source;
            }

            @Override
            public ParticipantPool getResource() {
                return source;
            }
        };
    }
}
