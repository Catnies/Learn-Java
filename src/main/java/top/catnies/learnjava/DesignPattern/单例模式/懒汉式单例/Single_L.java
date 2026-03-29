package top.catnies.designpattern.单例模式.懒汉式单例;


import java.io.Serial;

/**
 * 懒汉式单例
 * 该单例模式通过静态方法获取对象, 但是该单例只会在第一次获取时创建对象.
 */
public class Single_L {

    //持有一个实例，但是在加载的时候不会自动赋值
    private static Single_L instance;

    private Single_L(){}
    public static Single_L getInstance(){
        // 如果没有对象才创建对象
        if (instance == null){
            // 使用同步锁, 确保在多线程下访问该类时, 也只能获得唯一实例.
            synchronized (Single_L.class){
                if (instance == null){
                    instance = new Single_L();
                }
            }
        }
        return instance;
    }


    public void work(){
        System.out.println("This ia a Single by L");
    }
}
