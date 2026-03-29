package top.catnies.designpattern.抽象工厂模式.资源;


import top.catnies.designpattern.抽象工厂模式.AbstractFactory;

/**
 * 工厂类
 * 该类用于创建各种 Fruit
 */
public class FruitSimpleFactory extends AbstractFactory {

    /**
     * 根据需要的 Type 自动选择所需的 People 类创建对象并返回
     * @param type 类型
     * @return 返回
     */
    @Override
    public Fruit getFruit(String type) {
        return switch (type) {
            case "Apple" -> new Apple();
            case "Banana" -> new Banana();
            case "Cherry" -> new Cherry();
            default -> null;
        };
    }

    @Override
    public Color getColor(String type) {
        return null;
    }
}
