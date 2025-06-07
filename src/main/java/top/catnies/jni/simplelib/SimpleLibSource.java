package top.catnies.jni.simplelib;


// JNI 调用 C++
// 1. 编写Java代码, 在方法中加入 native 关键字声明方法是一个JNI方法.
// 2. 使用 javac -h 生成类对应的 C++ 头文件.
// 3. 编写 C++ 函数，实现头文件.
// 4. 编译 C++ 为静态库, DLL 文件.
// 5. 使用 System.load 加载 DLL 库.
// 6. 调用 DLL 库中的函数.
//
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
