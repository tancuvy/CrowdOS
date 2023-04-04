package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.Decomposer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


class UnresolvableResolverGenerator implements Decomposer<Constraint> {
    private final ArrayList<Class<?>> argsClass;
    private final ArrayList<Object> args;
    private final Class<UnresolvableConstraint> ucClass;

    public UnresolvableResolverGenerator(ArrayList<Class<?>> argsClass,ArrayList<Object> args,Class<UnresolvableConstraint> ucClass) {
        this.argsClass = argsClass;
        this.args = args;
        this.ucClass = ucClass;
    }

    @Override
    public List<Constraint> trivialDecompose() {

        UnresolvableConstraint uc;
        try {
            int len = argsClass.size();
            Class<?>[] aClass = new Class[len];
            Object[] arg = new Object[len];
            for (int i = 0; i < len; i++) {
                aClass[i] = argsClass.get(i);
                arg[i] = args.get(i);
            }

            Constructor<UnresolvableConstraint> constructor = ucClass.getConstructor(aClass);
            uc = constructor.newInstance(arg);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return Collections.singletonList(uc);
    }

    @Override
    public List<Constraint> scaleDecompose(int scale) {
        System.err.println("This constraint cannot be decomposed!");
        return this.trivialDecompose();
    }
}

