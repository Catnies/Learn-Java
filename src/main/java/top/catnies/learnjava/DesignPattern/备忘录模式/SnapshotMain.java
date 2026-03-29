package top.catnies.designpattern.备忘录模式;


import lombok.Data;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/**
 * 备忘录模式
 * 在不违背封装原则的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态，以便之后恢复对象为先前的状态。
 * 例如, 游戏存档, 需要频繁撤销的地方.
 */
public class SnapshotMain {
    public static void main(String[] args) {

        // 先创建发起人和负责人
        Originator originator = new Originator();
        Caretaker caretaker = new Caretaker();

        // 程序出现变化
        originator.setState("状态A");

        // 用负责人去新建新的存档
        caretaker.save(originator.saveState());

        // 程序再次出现变化
        originator.setState("状态B");

        // 用发起人回溯存档
        originator.restoreState(caretaker.get());

    }
}

/**
 * 存档
 * 我们需要保存的对象, 这个对象里应该记录了当前程序的一个状态.
 * 并且我们可以通过这个状态对程序进行回溯, 类似数据类.
 */
@Data
class Memento  {
    private String state;
    public Memento(String state) {
        this.state = state;
    }
}


/**
 * 发起人
 * 发起人类更像 备忘录 的管理类.
 * 发起人类内部会保存一个当前状态, 并且拥有创建和回溯某个备忘录类的能力
 */
@Data
class Originator {
    private String state;

    // 保存
    public Memento saveState(){
        return new Memento(state);
    }

    // 回溯
    public void restoreState(Memento memento){
        state = memento.getState();
    }
}


/**
 * 存档负责人
 * 存档管理类内部维护了一个栈, 有新的备忘录则进行压栈, 如果需要回溯则进行 弹栈 .
 */
class Caretaker {
    private final Deque<Memento> stack = new ArrayDeque<>();

    public void save(Memento memento){
        System.out.println("程序保存中... 当前状态是" + memento.getState());
        stack.push(memento);
    }
    public Memento get(){
        if(stack.isEmpty()){
            System.out.println("Nothing!");
            return null;
        }
        Memento pop = stack.pop();
        System.out.println("程序存档提取中... 提取的存档状态是" + pop.getState());
        return pop;
    }
}