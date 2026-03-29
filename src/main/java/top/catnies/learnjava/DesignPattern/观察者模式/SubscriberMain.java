package top.catnies.designpattern.观察者模式;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订阅者模式
 * 订阅者模式是观察者模式的一个变种. 旨在将观察者和主题进行解耦, 由一个第三方组件去维护主题和对应的观察者.
 */
public class SubscriberMain {
    public static void main(String[] args) {

        EventBus eventBus = new EventBus();
        eventBus.register("agu", new UserA("Jones"));
        eventBus.register("agu", new UserA("Kana"));

        Stock stock = new Stock("agu",100.0, eventBus);
        stock.stockPriceChanged(105.0);

    }
}


/**
 * 我们需要先实现观察者, 此时也叫作订阅者.
 * 例如, 模拟股票场景, 观察者是每个客户, 当有股票的价格发送变化时, 就向对应的关注了这支股票的观察者们发送消息
 */
interface Subscriber {
    // 当观察者触发时, 会触发对应的事件代码.
    void onStockPriceChanged(String stockSymbol, double newPrice);
}
class UserA implements Subscriber {
    String userName;
    public UserA(String userName) {
        this.userName = userName;
    }
    @Override
    public void onStockPriceChanged(String stockSymbol, double newPrice) {
        System.out.println("Dear " + userName + "! Receiver Stock: " + stockSymbol + " Price Changed: " + newPrice);
    }
}


/**
 * 然后我们需要管理主题和观察者的 事件总线 类.
 * 此时主题已经不再需要去维护观察者了.
 * 这里的主题我们使用 String 表示.
 */
class EventBus {
    Map<String, List<Subscriber>> subscribers = new HashMap<>();

    // 订阅股票发送价格变动的消息
    public void register(String stockSymbol, Subscriber subscriber) {
        subscribers.computeIfAbsent(stockSymbol, v -> new ArrayList<>()).add(subscriber);
    }
    // 移除订阅
    public void unregister(String stockSymbol, Subscriber subscriber) {
        subscribers.get(stockSymbol).remove(subscriber);
    }

    // 发布事件, 也就是股票价格变化时触发
    public void publish(String stockSymbol, double newPrice) {
        List<Subscriber> users = subscribers.get(stockSymbol);
        for (Subscriber user : users) {
            user.onStockPriceChanged(stockSymbol, newPrice);
        }
    }
}

/**
 * 股票类, 也就是被监视的主题类
 */
class Stock{
    private final String stockSymbol;
    private double stockPrice;
    private final EventBus eventBus;
    public Stock(String stockSymbol, double stockPrice, EventBus eventBus) {
        this.stockSymbol = stockSymbol;
        this.stockPrice = stockPrice;
        this.eventBus = eventBus;
    }

    public void stockPriceChanged(double newPrice) {
        this.stockPrice = newPrice;
        eventBus.publish(stockSymbol, stockPrice);
    }
}
