package top.catnies.learnjava.ByteBuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class ByteBuddyClassProxyAnno {

    public static void main(String[] args) throws Exception {
        run();
    }


    // 原始类
    public static class ProxyClass {
      public String hello(String name) {
          return "hello " + name;
      }

    }


    public static class ProxyInterceptor {
        @RuntimeType
        public static String toString(@This Object instance) {
            return "intercept!";
        }

        @RuntimeType
        public static int hashCode(
                @This Object instance, // 代表调用该方法的实例
                @Origin Method method,  // 代表方法对象
                @SuperCall Callable<?> superCall // 这个才代表父类的方法
        ) throws Exception {
            System.out.println("调用hashCode之前输出一次: " + instance);
            System.out.println("查看一下方法对象: " + method.toString());
            System.out.println("调用一下原始方法: " + method.invoke(new String("666"))); // 这里不能传入自己instance, 否则实际上调用的还是自身, 会递归导致崩溃.
            System.out.println("调用一下父类方法: " + superCall.call());
            return 114514;
        }

        @RuntimeType
        public static String helloProxy(
                @This Object instance, // 代表调用该方法的实例
                @Origin Method method,  // 代表方法对象
                @SuperCall Callable<?> superCall, // 这个才代表父类的方法
                @AllArguments Object[] args, // 代表方法的所有参数
                @Argument(0) String name // 代表方法的第一个参数, 如果不加注解则必须将参数写在第一位, 不然无法匹配.
        ) throws Exception {
            Object result = superCall.call();
            System.out.println("父类方法拿到的值: " + result);
            return "hello proxy " + name;
        }

    }

    public static void run() throws Exception {
        Class<?> aClass = new ByteBuddy()
                .subclass(ProxyClass.class)
                .name("top.catnies.Hanabi")

                // 将一个toString方法代理到 ProxyInterceptor 类
                .method(ElementMatchers.named("toString")).intercept(MethodDelegation.to(ProxyInterceptor.class))
                .method(ElementMatchers.named("hashCode")).intercept(MethodDelegation.to(ProxyInterceptor.class))
                .method(ElementMatchers.named("hello")).intercept(MethodDelegation.to(ProxyInterceptor.class))

                .make()
                .load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();

        Object instance = aClass.getDeclaredConstructor().newInstance();
        System.out.println("instance.toString(): " + instance.toString() + "\n ------------");
        System.out.println("instance.hashCode(): " + instance.hashCode() + "\n ------------");

        Method hello = aClass.getMethod("hello", String.class);
        System.out.println("instance.hello()   : " + hello.invoke(instance, "catnies") + "\n ------------");
    }


}