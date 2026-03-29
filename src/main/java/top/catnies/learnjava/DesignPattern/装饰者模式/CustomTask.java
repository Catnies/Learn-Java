package top.catnies.designpattern.装饰者模式;


/**
 * 需要被装饰的类, 例如当前类的 run 方法是无输出的, 我们需要给它增加一个返回值.
 * 同时, 希望这个类可以多个 accept 方法.
 */
public class CustomTask {

    public void cancel(){
        System.out.println("Task cancelled!");
    }

    public void run(){
        System.out.println("Run Run Run!!!!");
    }
}

/**
 * 创建一个类, 通过构造方法把需要被装饰的类传入到字段.
 * 然后照样"重写"一下原来类里有的方法, 从而实现对该类的功能拓展.
 */
class Decorator {
    private CustomTask customTask;

    public Decorator(CustomTask customTask) {
        this.customTask = customTask;
    }

    // 直接使用被装饰的对象的方法.
    public void cancel(){
        customTask.cancel();
    }
    // 创造同名方法但是可以修改方法的返回值等.
    public String run(){
        customTask.run();
        return "Task is Run!";
    }
    // 拓展新的方法
    public void accept(){
        System.out.println(customTask.getClass().getName());
    }
}

/**
 * 测试代码
 */
class main{
    public static void main(String[] args) {
        CustomTask customTask = new CustomTask();
        Decorator decorator = new Decorator(customTask);
        decorator.run();
        decorator.accept();
    }
}
