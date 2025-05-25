package cn.chengzhiya.mhdfverify;

public final class Verify {

    static {
        System.load("F:\\VsCode Projects\\C++-Learn\\R04-MDVerifyJNI\\build\\bin\\libmdverifyjni.dll");
    }

    public native String check(String host,String user,String password, String plugin);

    public static void main(String[] args) {
        Verify mdVerify = new Verify();
        String result = mdVerify.check(
            "https://www.baidu.com",
            "那谁",
            "1145141919810",
            "Deep-Dark-Fantasy"
        );
        System.out.println("验证结果: " + result);
    }

}
