package top.catnies.learnjava.ByteBuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;


// 使用 ByteBuddy 创建一个实现接口的类
public class ByteBuddyClassWithInterface {
    public static void main(String[] args) throws Exception {
        run();
    }

    // 需要实现的接口
    public static interface MyInterface {
        public void sayHello();
    }

    public interface LifecycleInterface {
        void initialize();
        void destroy();
    }


    // 负责代理实现的委托类
    public static class ProxyIntercept {
        @RuntimeType
        public static void sayHello_changeName(
                @This Object instance
        ) {
            System.out.println("Hello!");
        }

        @RuntimeType
        public static void initialize(@This Object instance) {
            System.out.println("初始化: " + instance.getClass().getSimpleName());
        }

        @RuntimeType
        public static void destroy(@This Object instance) {
            System.out.println("销毁: " + instance.getClass().getSimpleName());
        }
    }


    public static void run() throws Exception {
        Class<?> aClass = new ByteBuddy()
                .subclass(Object.class)
                .name("top.catnie.Ame")

                // 继承接口
                .implement(MyInterface.class, LifecycleInterface.class)

                // 实现接口
                // 委托方法的匹配默认基于方法签名（名称、参数类型、返回类型）
                .method(ElementMatchers.named("sayHello")).intercept(MethodCall.invoke(ProxyIntercept.class.getMethod("sayHello_changeName", Object.class)).withThis()) // 如果是需要强制显示指定方法, 推荐使用MethodCall去匹配
                .method(ElementMatchers.named("initialize")).intercept(MethodDelegation.to(ProxyIntercept.class))
                .method(ElementMatchers.named("destroy")).intercept(MethodDelegation.to(ProxyIntercept.class))

                .make()
                .load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();

        Object instance = aClass.getDeclaredConstructor().newInstance();

        // 将实例转换为接口类型，这样可以直接调用接口方法
        MyInterface myInterface = (MyInterface) instance;
        LifecycleInterface lifecycle = (LifecycleInterface) instance;
        myInterface.sayHello();
        lifecycle.initialize();
        lifecycle.destroy();
    }

}


