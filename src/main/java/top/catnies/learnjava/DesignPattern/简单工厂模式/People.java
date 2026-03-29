package top.catnies.designpattern.简单工厂模式;


/**
 * 资源类: 我们有3个类可以创建.
 * 接口 + 实现类
 */
public interface People {
    void work();
}

class Coder implements People {
    @Override
    public void work() {
        System.out.println("work -> write code!");
    }
}

class Painter implements People {
    @Override
    public void work() {
        System.out.println("work -> draw!");
    }
}

class Doctor implements People {
    @Override
    public void work() {
        System.out.println("work -> heal!");
    }
}
