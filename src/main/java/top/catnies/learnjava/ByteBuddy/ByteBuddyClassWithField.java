package top.catnies.learnjava.ByteBuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.*;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

 // 使用 ByteBuddy 创建一个类, 并且为其添加字段和注解。
public class ByteBuddyClassWithField {

    public static void main(String[] args) throws Exception {
        createClass_DefineField();
    }

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

                .make() // 生成字节码

                // 使用类加载器, 将字节码加载到JVM.
                // 这里的第二个参数是加载策略, 这是一个重要的参数, Byte Buddy 提供了几种类加载策略，这些策略定义在 ClassLoadingStrategy.Default 中.
                // WRAPPER      ->  创建一个新的 ClassLoader 来加载动态生成的类型。(默认)
                // CHILD_FIRST  ->  创建一个子类优先加载的 ClassLoader，即打破了双亲委派模型。
                // INJECTION    ->  使用反射将动态生成的类型直接注入到当前 ClassLoader 中。
                .load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded(); // 获取 Class 对象


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