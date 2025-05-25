package top.catnies.simplelib;

public class SimpleLibSource {

    static {
        System.load("F:\\VsCode Projects\\C++-Learn\\R03-SimpleJNI\\build_auto\\bin\\libsimplejni.dll");
    }
    public native int add(int a, int b);


    public static void main(String[] args) {
        SimpleLibSource simpleLib = new SimpleLibSource();
        int result = simpleLib.add(2, 3);
        System.out.println(result);
    }

}
