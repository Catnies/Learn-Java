package top.catnies.designpattern.抽象工厂模式.资源;


import top.catnies.designpattern.抽象工厂模式.AbstractFactory;

/**
 * 工厂类
 * 该类用于创建各种 Color
 */
public class ColorSimpleFactory extends AbstractFactory {

    @Override
    public Fruit getFruit(String type) {
        return null;
    }

    /**
     * 根据需要的 Type 自动选择所需的 Color 类创建对象并返回
     * @param type 类型
     * @return 返回
     */
    @Override
    public Color getColor(String type) {
        return switch (type) {
            case "Red" -> new Red();
            case "Blue" -> new Blue();
            case "Green" -> new Green();
            default -> null;
        };
    }
}
