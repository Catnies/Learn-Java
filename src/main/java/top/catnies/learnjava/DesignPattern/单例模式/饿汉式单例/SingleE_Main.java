package top.catnies.designpattern.单例模式.饿汉式单例;

/**
 * 运行测试
 */
public class SingleE_Main {

    public static void main(String[] args) {
        // 通过内部的静态方法去获取对象.
        Single_E instance = Single_E.getInstance();
        instance.work();
    }

}
