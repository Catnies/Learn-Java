package top.catnies.designpattern.简单工厂模式;


/**
 * 工厂类
 * 该类用于创建各种 People
 */
public class PeopleSimpleFactory {

    /**
     * 根据需要的 Type 自动选择所需的 People 类创建对象并返回
     * @param type 类型
     * @return 返回
     */
    public static People createPeople(String type) {
        return switch (type) {
            case "Coder" -> new Coder();
            case "Painter" -> new Painter();
            case "Doctor" -> new Doctor();
            default -> null;
        };
    }

}
