package cn.crowdos.kernel.constraint;
import cn.crowdos.kernel.Decomposer;
import java.util.ArrayList;

public class UnresolvableConstraint implements Constraint {

    @Override
    public Decomposer<Constraint> decomposer() {
        ArrayList<Class<?>> argsClass = new ArrayList<>();
        ArrayList<Object> args = new ArrayList<>();
        Class<UnresolvableConstraint> urgClass = UnresolvableConstraint.class;
//      Field[] fields = urgClass.getDeclaredFields();
        return new UnresolvableResolverGenerator(argsClass,args,urgClass);
    }

    @Override
    public boolean satisfy(Condition condition) {
        return false;
    }

    @Override
    public Class<? extends Condition> getConditionClass() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

}
