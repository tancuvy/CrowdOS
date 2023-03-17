package cn.crowdos.kernel.constraint;


import cn.crowdos.kernel.Decomposer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CON2 implements Constraint{


    Integer a;
    String name;

    public CON2(Integer a, String name) {
        this.a = a;
        this.name = name;
    }

    public Integer getA() {
        return a;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "CON2{" +
                "a=" + a +
                ", name='" + name + '\'' +
                '}';
    }

    //重写Decomposer接口的decomposer方法
    @Override
    public Decomposer<Constraint> decomposer() {

        ArrayList<Class<?>> argsClass = new ArrayList<>();//第一个list是为了暂存有参构造中参数的个各类型
        ArrayList<Object> args = new ArrayList<>(); //第二个list是为了暂存创建对象是参数传入的真实的值

        Class<CON2> con2Class = CON2.class; //获取class对象

        Field []fields = con2Class.getDeclaredFields();
        for (Field field :fields){
            Class<?> fieldType = field.getType();
            argsClass.add(fieldType);
        }

        Integer value1 = this.getA();
        String value2 = this.getName();
        args.add(value1);
        args.add(value2);
        return
                new Decomposer<Constraint>() //返回的是实现Decomposer接口的无名类的对象
        { //匿名内部类

            @Override
            public List<Constraint> trivialDecompose(){
                CON2 con2 ;
                try {
                    int len = argsClass.size();
                    Class<?>[] aClass = new Class[len];
                    Object [] arg = new Object[len];
                    for(int i = 0; i < len; i++){
                        aClass[i] = argsClass.get(i);
                        arg[i] = args.get(i);
                    }

                    Constructor<CON2> constructor = con2Class.getConstructor(aClass);
                    con2 = constructor.newInstance(arg);

                } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException |
                         IllegalArgumentException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                return Collections.singletonList(con2);
            }

            @Override
            public List<Constraint> scaleDecompose(int scale)  {
                System.err.println("Warning");
                return this.trivialDecompose();
            }
        };
    }

    public static void main(String[] args) {
        CON2 c = new CON2(17,"yyy");
        Decomposer<Constraint> dd = c.decomposer();
        List<Constraint> constraints = dd.trivialDecompose();
        System.out.println(constraints);
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
