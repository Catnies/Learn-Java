package top.catnies.learnjava.Reflection;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class UnsafeReflection {

    private static final Unsafe U;
    // 缓存偏移量，必须是 static final 让 JIT 知道这是常量
    private static final long STATIC_FINAL_FIELD_OFFSET;
    private static final Object STATIC_FINAL_FIELD_BASE;
    private static final long INSTANCE_FIELD_OFFSET;

    static {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            U = (Unsafe) unsafeField.get(null);

            // 1. 缓存目标类的实例字段偏移量
            Field instanceField = TargetClass.class.getDeclaredField("privateField");
            INSTANCE_FIELD_OFFSET = U.objectFieldOffset(instanceField);

            // 2. 缓存目标类的静态字段偏移量和基址
            Field staticField = TargetClass.class.getDeclaredField("STATIC_FINAL_FIELD");
            STATIC_FINAL_FIELD_OFFSET = U.staticFieldOffset(staticField);
            STATIC_FINAL_FIELD_BASE = U.staticFieldBase(staticField);

        } catch (Exception e) {
            throw new Error(e);
        }
    }

    // 目标 1：极其暴力的实例字段读取（没有反射方法调用，只有极其纯粹的内存屏障/取值）
    public static Object getPrivateField(TargetClass target) {
        return U.getObject(target, INSTANCE_FIELD_OFFSET);
    }

    // 目标 2：极其暴力的强制覆写现存对象的 static final 字段
    public static void forceSetStaticFinal(Object newValue) {
        // 无视所有 final 限制，直接按内存地址把值拍进去
        U.putObject(STATIC_FINAL_FIELD_BASE, STATIC_FINAL_FIELD_OFFSET, newValue);
    }


    public static void main(String[] args) throws Exception {
        TargetClass targetClass = new TargetClass();

        // 获取
        Object privateField = getPrivateField(targetClass);
        System.out.println("get private field: " + privateField);

        // 修改前
        System.out.println("Before: " + TargetClass.getStaticFinalField());

        // 通过反射获取字段的实际值
        Field field = TargetClass.class.getDeclaredField("STATIC_FINAL_FIELD");
        field.setAccessible(true);
        System.out.println("Before (via reflection): " + field.get(null));

        // 修改
        forceSetStaticFinal(new LinkedList<>());

        System.out.println("After: " + TargetClass.getStaticFinalField().getClass().getSimpleName());
        System.out.println("After (via reflection): " + field.get(null));

        // 验证内存中的值确实被修改了
        Field staticField = TargetClass.class.getDeclaredField("STATIC_FINAL_FIELD");
        long offset = U.staticFieldOffset(staticField);
        Object base = U.staticFieldBase(staticField);
        System.out.println("After (via Unsafe get): " + U.getObject(base, offset));
    }
}
