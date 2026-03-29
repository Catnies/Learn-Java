package top.catnies.designpattern.单例模式.懒汉式单例;

/**
 * 测试运行
 */
public class SingleL_Main {

    public static void main(String[] args) {
        Single_L instance = Single_L.getInstance();
        instance.work();

        double i = 1.0 / 0.0;
    }

}
