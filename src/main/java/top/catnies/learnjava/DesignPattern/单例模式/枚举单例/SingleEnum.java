package top.catnies.designpattern.单例模式.枚举单例;

/**
 * Java 中的枚举类本身也是一个单例.
 */
public enum SingleEnum {
    SINGLE;

    public void work() {
        System.out.println("This is a single object bv enum!");
    }
}