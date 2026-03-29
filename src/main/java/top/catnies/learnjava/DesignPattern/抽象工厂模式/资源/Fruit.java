package top.catnies.designpattern.抽象工厂模式.资源;


/**
 * 资源类: 我们有3个类可以创建.
 * 接口 + 实现类
 */
public interface Fruit {
    void eat();
}

class Apple implements Fruit {
    @Override
    public void eat() {
        System.out.println("eat Apple!");
    }
}

class Banana implements Fruit {
    @Override
    public void eat() {
        System.out.println("eat Banana!");
    }
}

class Cherry implements Fruit {
    @Override
    public void eat() {
        System.out.println("eat Cherry!");
    }
}
