package top.catnies.learnjava.Reflection;

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