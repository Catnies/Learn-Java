package top.catnies.designpattern.简单工厂模式;

public class SimpleFactory_Main {

    public static void main(String[] args) {
        // 当我需要生产一个 Coder 对象时, 不再使用 new 进行生产.
        // 我可以 (获取简单工厂的对象 / 使用工厂的静态方法) 进行生产.
        People coder = PeopleSimpleFactory.createPeople("Coder");
        coder.work();
    }

}
