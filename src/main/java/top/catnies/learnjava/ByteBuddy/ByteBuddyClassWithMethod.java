package top.catnies.learnjava.ByteBuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Modifier;


// 使用 ByteBuddy 创建一个类, 并且为其添加一个方法。
public class ByteBuddyClassWithMethod {

    public static void main(String[] args) throws Exception {
        createClass_DefineMethod();
    }


    // 我们需要创建一个新的类, 实现方法, 将方法代码写入这个方法。
    public static class IceCreamMethodImplementations {
        @RuntimeType  // 处理类型转换, 如此方法定义了参数 String 类型, 所以我们在使用方法传入可以正常匹配. 但是如果String变成了Object, 则就无法匹配, 而@RuntimeType告诉匹配器, 应该尝试进行类型转换, 使其匹配.
        public static String eat(@Argument(0) String food) {  // @Argument 指定参数位置
            System.out.println("I'm so hungry!, eat " + food + " !");
            return "I'm so hungry!, eat " + food + " !";
        }

        @RuntimeType
        public void drink(@This Object instance) { // 这里的 @This 可以用在非实例方法内, 代表调用这个方法的示例对象.
            System.out.println("I'm so thirsty, drinking..., instance is :" + instance);
        }
    }


    public static void createClass_DefineMethod() throws Exception {
        IceCreamMethodImplementations interceptor = new IceCreamMethodImplementations();
        Class<?> clazz = new ByteBuddy()
                .subclass(Object.class)  // 指定新类继承自哪个类
                .name("top.catnies.IceCream") // 设置类限定名


                // method 指令允许我们选择任何想要覆盖的方法。
                // 通过传递 ElementMatcher 来筛选决定需要覆盖的方法。
                // ByteBuddy 提供了许多预定义的匹配器，这些匹配器都收集在 ElementMatchers 类中。
                // 我们使用了 named 方法匹配器，它通过精确的方法名来选择方法。同时，匹配器是可以组合的，我们可以更详细地描述方法选择。
                .method(ElementMatchers.named("toString")
                        .and(ElementMatchers.returns(String.class))
                        .and(ElementMatchers.takesArguments(0)))
                    .intercept(
                            FixedValue.value("Wow, This is Asahi!")
                    ) // 选择方法并定义其行为, 这里是重写ToString方法.


                // 实际上, 在委托方法时我们有一些个方法:
                // 1. MethodDelegation.to(委托类, [字段])
                // 2. MethodDelegation.to(实例, [字段])
                // 3. MethodCall.invoke(方法)
                // 4. MethodCall.invoke(ElementMatchers捕捉自身方法)

                // defineMethod 可以定义新方法，你需要提供 方法名称, 返回值类型, 方法可访问权限。
                .defineMethod("eat", String.class, Modifier.PUBLIC | Modifier.STATIC)
                    .withParameter(String.class, "food") // 设置需要匹配的方法参数
                    .intercept(MethodDelegation.to(IceCreamMethodImplementations.class)) // 在执行时委托给 IceCreamMethodImplementations 类.
                .defineMethod("drink", void.class, Modifier.PUBLIC)
                    .intercept(MethodDelegation.to(interceptor))// 在执行时委托给之前创建的对象, 因为这不是一个静态方法.

                // 设置一个调用链, 先调用 eat 再调用 drink .
                .defineMethod("drinkAndEat", String.class, Modifier.PUBLIC)
                    .withParameter(String.class, "food")
                    .intercept(
                        MethodCall.invoke(ElementMatchers.named("drink")) // 先执行第一个方法, 这个方法在本类捕捉 drink 方法.
                            .andThen(
                                // 执行完成后执行第二个方法, 执行eat方法.
                                MethodCall.invoke(ElementMatchers.named("eat"))
                                        .withArgument(0)  // 使用drinkAndEat方法里传入的第一个参数
                            )
                    )


                .make() // 生成字节码

                // 使用类加载器, 将字节码加载到JVM.
                // 这里的第二个参数是加载策略, 这是一个重要的参数, Byte Buddy 提供了几种类加载策略，这些策略定义在 ClassLoadingStrategy.Default 中.
                // WRAPPER      ->  创建一个新的 ClassLoader 来加载动态生成的类型。(默认)
                // CHILD_FIRST  ->  创建一个子类优先加载的 ClassLoader，即打破了双亲委派模型。
                // INJECTION    ->  使用反射将动态生成的类型直接注入到当前 ClassLoader 中。
                .load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded(); // 获取 Class 对象



        // 创建对象, 然后调用方法.
        Object instance = clazz.getDeclaredConstructor().newInstance();
        System.out.println("\n" + "准备调用方法 eat: ");
        clazz.getDeclaredMethod("eat", String.class).invoke(null, "ice cream"); // 静态方法不需要对象.

        System.out.println("\n" + "准备调用方法 drink: ");
        clazz.getDeclaredMethod("drink").invoke(instance);

        System.out.println("\n" + "准备调用方法 drinkAndEat: ");
        clazz.getDeclaredMethod("drinkAndEat", String.class).invoke(instance, "ice cream");

        System.out.println("\n" + "打印对象: " + instance.toString()); // 因为重写了 toString , 所以返回的应该是自定义的行为.
    }
}
