package top.catnies.learnjava.Reflection;

import sun.misc.Unsafe;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.util.function.Function;

public class PerformanceComparison {

    private static final Unsafe U;
    private static final long INSTANCE_FIELD_OFFSET;
    private static final Field REFLECTION_FIELD;
    private static final MethodHandle METHOD_HANDLE;

    static {
        try {
            // 1. 初始化 Unsafe
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            U = (Unsafe) unsafeField.get(null);

            Field instanceField = TargetClass.class.getDeclaredField("privateField");
            INSTANCE_FIELD_OFFSET = U.objectFieldOffset(instanceField);

            // 2. 准备普通反射
            REFLECTION_FIELD = TargetClass.class.getDeclaredField("privateField");
            REFLECTION_FIELD.setAccessible(true);

            // 3. 准备 MethodHandle
            MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(TargetClass.class, MethodHandles.lookup());
            METHOD_HANDLE = lookup.findGetter(TargetClass.class, "privateField", String.class);

        } catch (Throwable e) {
            throw new Error(e);
        }
    }

    // 预热和测试时使用这个变量，防止 JIT 优化掉整个循环
    public static Object blackhole;

    public static void main(String[] args) throws Throwable {
        TargetClass target = new TargetClass();
        int warmUp = 200_000;
        int iterations = 10_000_000; // 增加次数，拉开差距

        System.out.println("预热 JVM ...");
        for (int i = 0; i < warmUp; i++) {
            blackhole = U.getObject(target, INSTANCE_FIELD_OFFSET);
            blackhole = REFLECTION_FIELD.get(target);
            blackhole = METHOD_HANDLE.invoke(target);
            blackhole = target.getPrivateFieldForTest();
        }

        System.out.println("\n========== 性能测试开始 (10,000,000次) ==========");

        // 1. 基准测试
        runTest("原生调用 (Baseline)", iterations, () -> {
            blackhole = target.getPrivateFieldForTest();
        });

        // 3. Unsafe
        runTest("Unsafe 内存访问", iterations, () -> {
            blackhole = U.getObject(target, INSTANCE_FIELD_OFFSET);
        });

        // 2. Public 直接访问
        runTest("Public调用", iterations, () -> {
            blackhole = target.privateField;
        });

        // 4. MethodHandle
        runTest("MethodHandle", iterations, () -> {
            try {
                blackhole = METHOD_HANDLE.invoke(target);
            } catch (Throwable e) {}
        });

        // 5. 反射
        runTest("普通反射", iterations, () -> {
            try {
                blackhole = REFLECTION_FIELD.get(target);
            } catch (IllegalAccessException e) {}
        });
    }

    private static void runTest(String name, int iterations, Runnable task) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            task.run();
        }
        long duration = System.nanoTime() - start;
        System.out.printf("%-25s | 平均耗时: %6.2f ns%n", name, (double) duration / iterations);
    }
}