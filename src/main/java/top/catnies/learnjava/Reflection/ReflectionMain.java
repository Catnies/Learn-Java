package top.catnies.learnjava.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionMain {

    public static void main(String[] args) throws Exception {
        createPrivateClass();
        getPrivateFieldAndMethod();
        getAllSuperClassAndInterface();
    }


    // 使用反射创建一个私有类对象
    public static void createPrivateClass() throws Exception {
        // 1. 想办法获取对象的Class.
        Class<?> clazz = Class.forName("top.catnies.learnjava.Reflection.PrivateClass");
        // 2. 获取构造函数, 并实例化对象.
        Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, int.class);
        Object instance = constructor.newInstance("catnies", 666);
        PrivateClass catnies = (PrivateClass)instance;
    }


    // 获取一个类的私有字段和方法.
    public static void getPrivateFieldAndMethod() throws Exception {
        PrivateClass catnies = new PrivateClass("catnies", 18);

        // 1. 想办法获取对象的Class.
        Class<?> clazz = catnies.getClass();    // 使用对象获取
        Class<?> clazz_2 = Class.forName("top.catnies.learnjava.Reflection.PrivateClass"); // 使用全类名获取.

        // 2. 获取Class中的字段对象.
        Field name_field = clazz.getDeclaredField("name"); // 注意, 要获取私有字段, 必须先使用getDeclaredField获取.
        Field age_field = clazz.getDeclaredField("age");

        // 3. 获取私有字段的值.
        name_field.setAccessible(true);         // 先使用setAccessible确保可以访问私有字段.
        String name = (String)name_field.get(catnies);  // 使用字段的get方法,传入对象, 获取字段的值.
        System.out.println("catnies name: " +  name);
        name_field.set(catnies, "catnies_new");  // 使用字段的set方法,传入对象, 设置字段的值.
        System.out.println("catnies name: " + name_field.get(catnies));

        // 4. 获取Class中的方法对象.
        Method agePlusTwo_method = clazz_2.getDeclaredMethod("getAgePlusTwo");

        // 5. 运行私有方法
        agePlusTwo_method.setAccessible(true);  // 先使用setAccessible确保可以访问私有方法.
        int agePlusTwo = (int) agePlusTwo_method.invoke(catnies);   // 使用方法的invoke方法,传入对象, 执行方法.
        System.out.println("catnies age: " + agePlusTwo);
    }


    // 获取类的所有父类和接口
    public static void getAllSuperClassAndInterface() throws Exception {
        // 1. 想办法获取对象的Class.
        Class<?> clazz = Class.forName("top.catnies.learnjava.Reflection.PrivateClass");
        // 2. 获取Class中的父类和接口.
        Class<?>[] interfaces = clazz.getInterfaces();
        Class<?> superclass = clazz.getSuperclass();

        // 3. 输出结果.
        System.out.println("interfaces: ");
        for (Class<?> i : interfaces) {
            System.out.println(i.getName());
        }
        System.out.println("superclass: " + superclass.getName());
    }
}
