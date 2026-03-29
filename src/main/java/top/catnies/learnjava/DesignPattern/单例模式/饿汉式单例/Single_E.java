package top.catnies.designpattern.单例模式.饿汉式单例;

/**
 * 饿汉式单例
 * 该单例模式在类加载时, 便会自动创建一个实例绑定在静态字段中.
 * 无法通过构造函数创建, 只能通过内部提供的静态方法获取.
 */
public class Single_E {

    // volatile 保证内存可见，保证有序性
    private volatile static Single_E instance = new Single_E();

    // 私有构造函数确保无法被外部创建.
    private Single_E() {}
    public static Single_E getInstance() {
        return instance;
    }

    public void work(){
        System.out.println("This is a Single by e han!");
    }

}
