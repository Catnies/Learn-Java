package top.catnies.designpattern.访问者模式;


import lombok.Data;

/**
 * 访问者模式
 * 它允许你在不修改现有类结构的情况下，为类添加新的操作。
 * 这种模式可以实现良好的解耦和扩展性，尤其适用于在现有类层次结构中添加新功能的情况。
 *
 * 访问者模式, 可以允许不同的访问者(视角)对一个或多个对象有不同的解释.
 */
public class VisitorMain {
    public static void main(String[] args) {
        Human human = new Human(28, "张三");
        Animal animal = new Animal("旺财", 5);

        FriendAgeVisitor friendAgeVisitor = new FriendAgeVisitor();
        DoctorAgeVisitor doctorAgeVisitor = new DoctorAgeVisitor();

        friendAgeVisitor.visitHuman(human);
        friendAgeVisitor.visitAnimal(animal);

        doctorAgeVisitor.visitHuman(human);
        doctorAgeVisitor.visitAnimal(animal);
    }
}


/**
 * 创建访问者视角.
 * 例如我们可以有访问年龄访问者.
 * 它们都可以对人或者动物进行访问, 但是因为访问者和被访问的对象不同, 而获取不同的值.
 */
interface Visitor {
    void visitHuman(Human human);
    void visitAnimal(Animal animal);
}
class FriendAgeVisitor implements Visitor {
    @Override
    public void visitHuman(Human human) {
        System.out.println("人的年龄永远18岁!");
    }

    @Override
    public void visitAnimal(Animal animal) {
        System.out.println("动物年龄你猜啊!");
    }
}
class DoctorAgeVisitor implements Visitor {
    @Override
    public void visitHuman(Human human) {
        System.out.println("我的年龄是" + human.age);
    }
    @Override
    public void visitAnimal(Animal animal) {
        System.out.println("动物年龄是 " + animal.age);
    }
}


/**
 * 被访问的对象
 */
@Data
class Human {
    int age;
    String name;

    public Human(int age, String name) {
        this.age = age;
        this.name = name;
    }
}
@Data
class Animal {
    String name;
    int age;
    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
