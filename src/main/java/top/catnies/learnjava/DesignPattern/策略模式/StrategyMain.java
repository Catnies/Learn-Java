package top.catnies.designpattern.策略模式;

import lombok.Data;

/**
 * 策略模式
 * 定义一族算法类，将每个算法分别封装起来，让它们可以互相替换。
 * 策略模式可以使算法的变化独立于使用它们的调用方.
 *
 * 策略模式可以允许在对象运行中动态替换行为算法.
 */
public class StrategyMain {
    public static void main(String[] args) {
        FlyStrategy flyStrategy = new FlyStrategy();
        SwimStrategy swimStrategy = new SwimStrategy();

        User zs = new User("张三", flyStrategy);
        zs.work();
        zs.setStrategy(swimStrategy);
        zs.work();


    }
}


/**
 * 定义一个策略接口
 * 例如当前有飞行和游泳两种策略.
 */
interface Strategy {
    void run();
}
class FlyStrategy implements Strategy {
    @Override
    public void run() {
        System.out.println("I am flying!!!");
    }
}
class SwimStrategy implements Strategy {
    @Override
    public void run() {
        System.out.println("I am swimming!!!");
    }
}


/**
 * 需要使用策略的类, 这个User会使用飞行或者游泳.
 * 当需要飞行的时候飞行, 需要游泳的时候游泳.
 * 我们可以在运行时动态 Set 内部的策略类
 */
@Data
class User{
    private String name;
    private Strategy strategy;
    public User(String name, Strategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    public void work(){
        strategy.run();
    }
}