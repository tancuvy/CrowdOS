package cn.crowdos.kernel.constraint;
import cn.crowdos.kernel.Decomposer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>In <i>Mobile CrowdSensing</i> (MCS), there are a lots of constraints,
 * which vary in type.For decomposable constraints, e.g. do something on a
 * certain date in a certain year.This can be broken down into specific
 * temporal and spatial constraints.In addition, there are a number of
 * non-decomposable constraints.For this, We use reflection to return the
 * indecomposable constraint decomposer generator we need by getting
 * the Class object of the class created by the upper level developer.</p>
 *
 * <p>The constraints in any real MCS problem need to be mapped directly to
 * a class that implements the {@code Constraint} interface or extend from
 * some abstract Constraint class, such as IndecomposableConstraint</p>
 *
 * Each IndecomposableConstraint can map to a class that implement interface {@code Constraint}. For example, the
 * code for IndecomposableConstraint 1 is shown below.
 *
 * <pre>{@code
 * import cn.crowdos.kernel.Decomposer;
 * import java.util.ArrayList;
 *
 * public class IndecomposableConstraint implements Constraint {
 *
 *   @Override
 *   public Decomposer<Constraint> decomposer() {
 *      ArrayList<Class<?>> argsClass = new ArrayList<>();
 *      ArrayList<Object> args = new ArrayList<>();
 *      Class<IndecomposableConstraint> urgClass = IndecomposableConstraint.class;
 *      Field[] fields = urgClass.getDeclaredFields();
 *          return new IndecomposableDecomposerGenerator(argsClass,args,urgClass);
 *      }
 *
 *      ... // other methods
 *   }
 * }</pre>
 * <p>After that the constraint class will be involved in the kernel's task
 * allocation process.</p>
 *
 * @since 1.0.2
 * @author yuzy
 */
class IndecomposableDecomposerGenerator implements Decomposer<Constraint> {
    private final ArrayList<Class<?>> argsClass;
    private final ArrayList<Object> args;
    private final Class<Constraint> ucClass;

    public IndecomposableDecomposerGenerator(ArrayList<Class<?>> argsClass, ArrayList<Object> args, Class<Constraint> ucClass) {
        this.argsClass = argsClass;
        this.args = args;
        this.ucClass = ucClass;
    }

    @Override
    public List<Constraint> trivialDecompose() {

        Constraint uc;
        try {
            int len = argsClass.size();
            Class<?>[] aClass = new Class[len];
            Object[] arg = new Object[len];
            for (int i = 0; i < len; i++) {
                aClass[i] = argsClass.get(i);
                arg[i] = args.get(i);
            }

            Constructor<Constraint> constructor = ucClass.getConstructor(aClass);
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

