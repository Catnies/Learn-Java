package top.catnies.designpattern.抽象工厂模式;

import top.catnies.designpattern.抽象工厂模式.资源.Color;

public class AbstractFactory_Main {

    public static void main(String[] args) {
        // 我们可以先获取一个全局的工厂, 然后获取具体所需的工厂, 最后通过工厂获取需要的对象.
        AbstractFactory colorFactory = FactoryProducer.getFactory("Color");

        Color redColor = colorFactory.getColor("Red");
        redColor.work();
        Color blueColor = colorFactory.getColor("Blue");
        blueColor.work();

        //...
    }

}
