package top.catnies.designpattern.观察者模式;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 观察者模式
 * 允许对象间存在一对多的依赖关系，当一个对象的状态发生改变时，所有依赖它的对象都会得到通知并自动更新。
 * 在这种模式中，发生状态改变的对象被称为“主题”（Subject），依赖它的对象被称为“观察者”（Observer）。
 */
public class ObserverMain {
    public static void main(String[] args) {
        Subject subject = new Subject("first");
        subject.addObserver(new FirstObserver());
        subject.addObserver(new SecondObserver());

        subject.setState("first");
        subject.notifyObservers();

        subject.setState("second");
        subject.notifyObservers();
    }
}


/**
 * 创建一个主题接口和实现类, 主题类应当在状态改变时通知其观察者类
 */
interface SubjectInterface<T>{
    void addObserver(Observer<T> observer);
    void removeObserver(Observer<T> observer);
    void notifyObservers();
}
class Subject<T> implements SubjectInterface<T>{
    // 记录当前主题的状态
    @Getter
    protected T state;
    public Subject(T state) {
        this.state = state;
    }
    // 维护观察者们
    protected List<Observer<T>> observers = new CopyOnWriteArrayList<>();
    // 提供观察者们的增删改查功能
    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }
    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }
    // 每当状态发生变化时, 我们应当通知所有的观察者们
    public void notifyObservers() {
        for (Observer<T> observer : observers) {
            observer.update(state);
        }
    }
    // 在更新时自动通知观察者们
    public void setState(T state) {
        this.state = state;
        notifyObservers();
    }
}


/**
 * 定义观察者类, 并且观察者具有更新方法.
 * 当主题更新时, 主题会调用观察者们的更新方法.
 */
interface Observer<T>{
    void update(T state);
}
class FirstObserver implements Observer<String>{
    private String state;

    @Override
    public void update(String state) {
        this.state = state;
        run();
    }

    public void run(){
        if ("first".equals(state)) {
            System.out.println(state);
        } else {
            System.out.println("the first observer is not work!");
        }
    }
}
class SecondObserver implements Observer<String>{
    private String state;

    @Override
    public void update(String state) {
        this.state = state;
        run();
    }

    public void run(){
        if ("second".equals(state)) {
            System.out.println(state);
        } else {
            System.out.println("the second observer is not work!");
        }
    }
}