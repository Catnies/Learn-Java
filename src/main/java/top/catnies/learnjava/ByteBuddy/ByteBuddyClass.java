package top.catnies.learnjava.ByteBuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.StubMethod;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.File;
import java.lang.reflect.Modifier;


// 使用 ByteBuddy 创建一个简单的类。
public class ByteBuddyClass {

    public static void main(String[] args) throws Exception {
        createClass();
        System.out.println("---------------------------------");
        createInterface();
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
                .getLoaded();// 从加载完成类中获取具体的class对象.

        // 你会发现并不一样, 因为load导致的策略问题
        System.out.println(aClass.getClassLoader());
        System.out.println(ClassLoader.getSystemClassLoader());

        // 创建类
        Object instance = aClass.getDeclaredConstructor().newInstance();
        System.out.println("instance: " + instance);
    }


    // 创建一个接口类, 然后再创建一个实现了这个接口的类.
    public static void createInterface() throws Exception {
        Class<?> aClass = new ByteBuddy()
                .with(ClassFileVersion.JAVA_V21) // 设置生成出来的字节码的java版本.
                .makeInterface()
                .name("top.catnies.HanabiInterface") // 设置类名
                .defineMethod("say", void.class, Modifier.PUBLIC).intercept(StubMethod.INSTANCE) // 这是个空实现, 因为接口需要空实现
                .make()
                .load(Thread.currentThread().getContextClassLoader(), ClassLoadingStrategy.Default.INJECTION) // 这里使用的是 INJECTION 策略, 否则默认情况下会因为类加载器的可见性问题导致其他类无法找到它.
                .getLoaded();
        System.out.println(aClass.getName());

        DynamicType.Unloaded<Object> make = new ByteBuddy()
                .subclass(Object.class)
                .implement(aClass)
                .name("top.catnies.HanabiImpl")
                .method(ElementMatchers.named("say")).intercept(FixedValue.originType())
                .make();

        // 子加载器可以看到父加载器的所有类, 但是父看不到子加载的类, 这里的Wapper策略实际上是在 App 下的 子 加载器, 同时接口类使用的是 Injection 策略, 也就是 App 加载器, 所以不同类加载器也可查找到.
        Class<?> aClass1 = make.load(Thread.currentThread().getContextClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        System.out.println(aClass1.getName());

        // 我们还可以将动态生成的Class保存到文件!
        File file = new File("F:\\IDEA Projects\\StudyProjects\\Learn-Java\\src\\main\\resources\\ByteBuddy");
        if (file.exists()) {
            System.out.println("文件存在");
            make.saveIn(file);
        } else {
            System.out.println("文件不存在");
        }
    }
}
