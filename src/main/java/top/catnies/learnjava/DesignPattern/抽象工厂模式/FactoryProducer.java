package top.catnies.designpattern.抽象工厂模式;


import top.catnies.designpattern.抽象工厂模式.资源.ColorSimpleFactory;
import top.catnies.designpattern.抽象工厂模式.资源.FruitSimpleFactory;

/**
 * 工厂生产者
 */
public class FactoryProducer {

    public static AbstractFactory getFactory(String choice){
        switch (choice) {
            case "Color" -> {
                return new ColorSimpleFactory();
            }
            case "Fruit" -> {
                return new FruitSimpleFactory();
            }
        }
        return null;
    }
}

