package top.catnies.designpattern.中介模式;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * 中介模式
 * 中介者模式定义了一个 中介对象 来封装一系列对象之间的交互。
 * 例如ABCD对象都需要互相发送消息, 那么我们可以定义一个 中介α ,所有消息都发送给中介, 让中介进行转发.
 * 使对象之间不需要显式地相互引用，从而使其耦合松散，且可以独立地改变它们之间的交互。
 */
public class MediatorMain {
    public static void main(String[] args) {
        // 创建发送者
        ConcreteSender sender1 = new ConcreteSender("Sender 1");
        ConcreteSender sender2 = new ConcreteSender("Sender 2");
        ConcreteSender sender3 = new ConcreteSender("Sender 3");
        ConcreteSender sender4 = new ConcreteSender("Sender 4");

        Mediator.addNewSender(sender1, sender2, sender3);

        sender1.sendMessage("woc 666");
    }
}


/**
 * 需要传输消息的对象.
 */
abstract class Sender{
    @Getter @Setter
    protected String name;
    public Sender(String name) {
        this.name = name;
    }
    abstract void sendMessage(String message);
    abstract void receiveMessage(Sender sender, String message);

}
class ConcreteSender extends Sender{
    public ConcreteSender(String name) {
        super(name);
    }
    @Override
    void sendMessage(String message) {
        Mediator.sendMessage(this, message);
    }
    @Override
    void receiveMessage(Sender sender, String message) {
        System.out.println(name + " received message: " + message + " from " + sender);
    }
}


/**
 * 中介类
 */
class Mediator{
    private static final Map<Sender, List<Sender>> mediatorList = new HashMap<>();

    // 给某个对象新增订阅目标
    public static void addNewSender(Sender sender, Sender... target) {
        List<Sender> senders = mediatorList.computeIfAbsent(sender, k -> new ArrayList<>());
        senders.addAll(Arrays.asList(target));
    }
    // 移除
    public static void removeSender(Sender sender, Sender... target) {
        List<Sender> senders = mediatorList.get(sender);
        if (senders != null) {
            senders.removeAll(Arrays.asList(target));
        }
    }
    // 发送消息
    public static void sendMessage(Sender sender, String message) {
        for (Sender receiver : mediatorList.get(sender)) {
            receiver.receiveMessage(sender, message);
        }
    }

}
