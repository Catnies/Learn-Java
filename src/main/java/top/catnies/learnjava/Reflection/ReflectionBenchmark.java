package top.catnies.learnjava.Reflection;

import net.momirealms.sparrow.reflection.clazz.SparrowClass;
import net.momirealms.sparrow.reflection.field.SField;
import net.momirealms.sparrow.reflection.field.SparrowField;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import sun.misc.Unsafe;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static net.momirealms.sparrow.reflection.field.matcher.FieldMatchers.fType;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class ReflectionBenchmark {

    private Unsafe U;
    private long INSTANCE_FIELD_OFFSET;

    private Field REFLECTION_FIELD;
    private MethodHandle FIELD_METHOD_HANDLE;

    // Java 9+ 引入的字段访问利器
    private VarHandle VAR_HANDLE;

    private Function<TargetClass, String> LAMBDA_METHOD_ACCESSOR;

    private TargetClass target;

    SparrowClass<TargetClass> sparrowClass;
    SField sparrowAsmField;

    @Setup(Level.Trial)
    public void setup() throws Throwable {
        target = new TargetClass();

        // 1. Unsafe (字段)
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        U = (Unsafe) unsafeField.get(null);
        Field instanceField = TargetClass.class.getDeclaredField("privateField");
        INSTANCE_FIELD_OFFSET = U.objectFieldOffset(instanceField);

        // 2. 普通反射 (字段)
        REFLECTION_FIELD = TargetClass.class.getDeclaredField("privateField");
        REFLECTION_FIELD.setAccessible(true);

        // 3. MethodHandle (字段 Getter)
        MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(TargetClass.class, MethodHandles.lookup());
        FIELD_METHOD_HANDLE = lookup.findGetter(TargetClass.class, "privateField", String.class);

        // 4. VarHandle (字段 - Java 9+ 推荐的高性能方案)
        VAR_HANDLE = lookup.findVarHandle(TargetClass.class, "privateField", String.class);

        // 5. LambdaMetafactory (只能代理"方法"，不能代理"字段")
        // 我们让它代理 getPrivateFieldForTest 方法来展示其方法调用的性能
        MethodHandle methodHandleForLMF = lookup.findVirtual(TargetClass.class, "getPrivateFieldForTest", MethodType.methodType(String.class));
        CallSite site = LambdaMetafactory.metafactory(
                lookup,
                "apply",
                MethodType.methodType(Function.class),
                MethodType.methodType(Object.class, Object.class),
                methodHandleForLMF, // 注意：这里传入的是 findVirtual 找到的方法，而不是 findGetter
                MethodType.methodType(String.class, TargetClass.class)
        );
        LAMBDA_METHOD_ACCESSOR = (Function<TargetClass, String>) site.getTarget().invokeExact();

        // 6. Sparrow
        sparrowClass = SparrowClass.of(TargetClass.class);
        SparrowField sparrowField = sparrowClass.getDeclaredSparrowField(fType(String.class), 0);
        sparrowAsmField = sparrowField.asm();
    }

    @Benchmark
    public void test01_NormalMethod(Blackhole bh) {
        bh.consume(target.getPrivateFieldForTest());
    }

    @Benchmark
    public void test02_NormalFieldAccess(Blackhole bh) {
        // 直接访问公开字段（最快基准）
        bh.consume(target.privateField);
    }

    @Benchmark
    public void test03_LambdaMetafactory_Method(Blackhole bh) {
        // 代理方法的极致性能
        bh.consume(LAMBDA_METHOD_ACCESSOR.apply(target));
    }

    @Benchmark
    public void test05_Unsafe_Field(Blackhole bh) {
        bh.consume(U.getObject(target, INSTANCE_FIELD_OFFSET));
    }

    @Benchmark
    public void test08_Sparrow_Field(Blackhole bh) {
        bh.consume(sparrowAsmField.get(target));
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ReflectionBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}