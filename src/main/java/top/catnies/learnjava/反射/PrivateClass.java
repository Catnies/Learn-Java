package top.catnies.learnjava.反射;

public class PrivateClass {
    private String name;
    private int age;

    public PrivateClass(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private int getAgePlusTwo() {
        return age + 2;
    }
}