package top.catnies.learnjava.Reflection;

public class TargetClass {
    public String privateField = "private!";
    private static final String STATIC_FINAL_FIELD = "STATIC_FINAL_FIELD";

    public static String getStaticFinalField() {
        return STATIC_FINAL_FIELD;
    }

    // 用于基准测试的公开方法
    public String getPrivateFieldForTest() {
        return privateField;
    }
}
