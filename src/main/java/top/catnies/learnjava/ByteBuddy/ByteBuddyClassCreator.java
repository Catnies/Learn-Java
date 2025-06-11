package top.catnies.learnjava.ByteBuddy;

import lombok.Data;
import lombok.NonNull;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.*;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings(value = "unchecked")
public class ByteBuddyClassCreator {
    public static void main(String[] args) throws Exception {
        createClass_DefineField();
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



    // 使用 ByteBuddy 创建一个类, 并且为其添加一个方法。
    // 我们需要创建一个新的类, 实现方法, 将方法代码写入这个方法。
    public static class IceCreamMethodImplementations {
        @RuntimeType  // 处理类型转换, 如果不加注解, 那么在设置方法参数也必须使用Object完全对应, 无法做到自动类型转换. 这个注解在处理一些通用的功能会非常有用.
        public static String eat(@Argument(0) Object food) {  // @Argument 指定参数位置
            System.out.println("I'm so hungry!, eat " + food + " !");
            return "I'm so hungry!, eat " + food + " !";
        }

        @RuntimeType
        public void drink() {
            System.out.println("I'm so thirsty, drinking...");
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
                    .intercept(FixedValue.value("Wow, This is Asahi!")) // 选择方法并定义其行为, 这里是重写ToString方法.


                // defineMethod 可以定义新方法，你需要提供 方法名称, 返回值类型, 方法可访问权限。
                .defineMethod("eat", String.class, Modifier.PUBLIC | Modifier.STATIC)
                    .withParameter(String.class, "food") // 设置需要匹配的方法参数
                    .intercept(MethodDelegation.to(IceCreamMethodImplementations.class)) // 在执行时委托给 IceCreamMethodImplementations 类.
                .defineMethod("drink", void.class, Modifier.PUBLIC)
                    .intercept(MethodDelegation.to(interceptor, "drink"))// 在执行时委托给之前创建的对象, 因为这不是一个静态方法.

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



    // 使用 ByteBuddy 创建一个类, 并且为其添加字段。
    // 同时, 设置一个构造函数, 使部分字段可以正常初始化.
    public static class CounterHelper {
        @RuntimeType
        public static void increment(Class<?> clazz) throws Exception {
            System.out.println("CounterHelper.increment() is called!");
            Field field = clazz.getField("allCounts");
            int counts = field.getInt(null);
            field.set(null, counts + 1);
        }

        @RuntimeType
        public static void increment() throws Exception {
            System.out.println("CounterHelper.increment() is called!");
            Class<?> aClass = Class.forName("top.catnies.Student");
            Field field = aClass.getField("allCounts");
            int counts = field.getInt(null);
            field.set(null, counts + 1);
        }
    }
    public static void createClass_DefineField() throws Exception {
        // 我们可以使用Builder去解决泛型字段的定义问题
        TypeDescription.Generic mapOfStringToInt = TypeDescription.Generic.Builder
            .parameterizedType(Map.class, String.class, Integer.class)
            .build();

        Class<?> aClass = new ByteBuddy()
                .subclass(Object.class)
                .name("top.catnies.Student")


                // annotateType 可以添加注解, 在这里添加的注解将会被加到类上
                .annotateType(AnnotationDescription.Builder.ofType(SuppressWarnings.class).defineArray("value", "unchecked").build())

                // defineField 可以定义新字段，你需要提供 字段名称, 类型, 访问权限。
                .defineField("name", String.class, Modifier.PUBLIC)
                    // 这里的注解将会被加到字段上
                    .annotateType(AnnotationDescription.Builder.ofType(Deprecated.class).build())
                .defineField("age", int.class, Modifier.PUBLIC).value(18) // 使用 value 可以设置字段的默认值.
                .defineField("allCounts", int.class, Modifier.PUBLIC | Modifier.STATIC).value(0) // 设置静态变量.
                .defineField("dataMap", mapOfStringToInt, Modifier.PUBLIC) // 创建一个泛型的Map<String, int>

                // defineConstructor 可以定义新的构造方法.
                .defineConstructor(Modifier.PUBLIC)
                    .withParameters(String.class, int.class)
                    .intercept(
                        // SuperMethodCall.INSTANCE -> 这是调用父类的构造函数, 但是因为我们是有参构造, 所以父类也会调用有参, 不过实际父类没有, 所以直接使用会抛出异常.
                        MethodCall.invoke(Object.class.getDeclaredConstructor()) // 调用父类的无参构造函数
                        .andThen(FieldAccessor.ofField("name").setsArgumentAt(0))
                        .andThen(FieldAccessor.ofField("age").setsArgumentAt(1))
                        .andThen(FieldAccessor.ofField("dataMap").setsValue(new HashMap<String, Integer>()))
                        // 自增处理, 这里只能将逻辑给委托给其他类的方法, 把自身类传递或者设置forName的ClassLoader和加载当前类的ClassLoader是一致的.
                        .andThen(MethodDelegation.to(CounterHelper.class))
                    )

                // 当然还需要get和set方法
                .defineMethod("getName", String.class, Modifier.PUBLIC)
                    .intercept(FieldAccessor.ofField("name"))
                .defineMethod("getAge", int.class, Modifier.PUBLIC)
                    .intercept(FieldAccessor.ofField("age"))
                .defineMethod("setName", void.class, Modifier.PUBLIC)
                    .withParameter(String.class, "name")
                    .intercept(FieldAccessor.ofField("name").setsArgumentAt(0))
                    // 这里会给方法加注解
                    .annotateMethod(AnnotationDescription.Builder.ofType(Deprecated.class).build())
                .defineMethod("setAge", void.class, Modifier.PUBLIC)
                    .withParameter(int.class, "age")
                    // 这里会给参数加注解
                    .annotateParameter(AnnotationDescription.Builder.ofType(Deprecated.class).build())
                    .intercept(FieldAccessor.ofField("age").setsArgumentAt(0))


                .make()
                .load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();


        // 查看类加载器
        System.out.println(aClass.getClassLoader());
        System.out.println(ClassLoader.getSystemClassLoader());

        // 尝试构建对象然后获取信息
        Object instance = aClass.getDeclaredConstructor(String.class, int.class).newInstance("catnies", 18);
        Object name = aClass.getMethod("getName").invoke(instance);
        Object age = aClass.getMethod("getAge").invoke(instance);
        System.out.println("name: " + name + ", age: " + age + ", allCounts: " + aClass.getField("allCounts").getInt(null));


        // 尝试修改后再输出
        aClass.getMethod("setName", String.class).invoke(instance, "catnies_new");
        aClass.getMethod("setAge", int.class).invoke(instance, 24);

        // 尝试为datamap字段添加新的数据
        HashMap<String, Integer> hashMap = (HashMap<String, Integer>)aClass.getField("dataMap").get(instance);
        hashMap.put("custom", 666);

        // 获取数据
        name = aClass.getMethod("getName").invoke(instance);
        age = aClass.getMethod("getAge").invoke(instance);
        System.out.println("name: " + name + ", age: " + age + "map: " + hashMap);
    }

}