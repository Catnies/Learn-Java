package top.catnies.designpattern.抽象工厂模式.资源;


/**
 * 资源类: 我们有3个类可以创建.
 * 接口 + 实现类
 */
public interface Color {
    void work();
}

class Red implements Color {
    @Override
    public void work() {
        System.out.println("Red!");
    }
}

class Blue implements Color {
    @Override
    public void work() {
        System.out.println("Blue!");
    }
}

class Green implements Color {
    @Override
    public void work() {
        System.out.println("Green!");
    }
}
