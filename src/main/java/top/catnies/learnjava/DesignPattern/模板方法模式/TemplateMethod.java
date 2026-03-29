package top.catnies.designpattern.模板方法模式;

/**
 * 模板方法模式
 * 模板方法模式旨在将固定的流程代码进行模板化, 但是将流程中的某一个部分留给子类定义.
 * 模板方法模式在一个方法中定义一个算法骨架，并将某些步骤推迟到子类中实现。模板方法模式可以让子类在不改变算法整体结构的情况下，重新定义算法中的某些步骤。
 * 例如有个方法需要依次执行 A B C, 其中A C是固定的, B则可以设置为抽象并交给子类实现.
 */
public class TemplateMethod {
    public static void main(String[] args) {
        ConcreteTemplateA concreteTemplateA = new ConcreteTemplateA();
        concreteTemplateA.run();
    }
}


/**
 * 定义一个模板类
 */
abstract class AbstractTemplate{
    protected void run(){
        methodA();
        methodB();
        methodC();
    }
    private void methodA(){
        System.out.println("Run A");
    }
    abstract void methodB();
    private void methodC(){
        System.out.println("Run C");
    }
}
class ConcreteTemplateA extends AbstractTemplate{
    @Override
    void methodB() {
        System.out.println("Special Run B");
    }
}
