package top.catnies.learnjava.ByteBuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;


// 使用 ByteBuddy 创建一个简单的类。
public class ByteBuddyClass {

    public static void main(String[] args) throws Exception {
        createClass();
    }

    // 使用 ByteBuddy 创建一个类。
    public static void createClass() throws Exception {
        // 定义一个Class
        Class<?> aClass = new ByteBuddy() // 创建一个ByteBuddy对象.
                .subclass(Object.class)// 指定新类继承自哪个类
                .name("top.catnies.Hanabi") // 设置类名
                .make() // 生成字节码

                // 使用类加载器, 将字节码加载到JVM.
                // 这里的第二个参数是加载策略, 这是一个重要的参数, Byte Buddy 提供了几种类加载策略，这些策略定义在 ClassLoadingStrategy.Default 中.
                // WRAPPER      ->  创建一个新的 ClassLoader 来加载动态生成的类型。(默认)
                // CHILD_FIRST  ->  创建一个子类优先加载的 ClassLoader，即打破了双亲委派模型。
                // INJECTION    ->  使用反射将动态生成的类型直接注入到当前 ClassLoader 中。
                .load(Thread.currentThread().getContextClassLoader(), ClassLoadingStrategy.Default.WRAPPER) // 使用类加载器, 将将字节码加载到JVM
                .getLoaded(); // 从加载完成类中获取具体的class对象.

        // 你会发现并不一样, 因为load导致的策略问题
        System.out.println(aClass.getClassLoader());
        System.out.println(ClassLoader.getSystemClassLoader());

        // 创建类
        Object instance = aClass.getDeclaredConstructor().newInstance();
        System.out.println("instance: " + instance);
    }
}
