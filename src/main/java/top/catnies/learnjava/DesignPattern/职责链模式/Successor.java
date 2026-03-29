package top.catnies.designpattern.职责链模式;

/**
 * 职责链模式
 * 将请求的发送和接收解耦，让多个接收对象都有机会处理这个请求。将这些接收对象串成一条链，并沿着这条链传递这个请求，让链上的接受对象依次处理它.
 * 例如, 我们可以设置一些接受处理字符串的链, 并传入字符串进行依次处理.
 */
public class Successor {
    public static void main(String[] args) {
        // 创建责任链, 每个责任链内部都可以设置下一个责任链, 并返回下一个责任链.
        // 与 Builder 不同的是, Builder 返回的总是当前对象, 而责任链返回的是字段中的"下一个"对象.
        Handle handle = new FirstHandle();
        handle.setNextHandle(new SecondHandle())
                .setNextHandle(new ThirdHandle())
                .setNextHandle(new FirstHandle());

        handle.handle(60);
    }
}

/**
 * 首先需要一个抽象类, 包含一个处理请求的抽象方法，并持有下一个处理者的引用。
 */
abstract class Handle {
    protected Handle nextHandle;

    // 一层套一层持续下去, 每一层都可以设置一个新的责任处理类.
    Handle setNextHandle(Handle nextHandle) {
        this.nextHandle = nextHandle;
        return nextHandle;
    }

    // 开始的方法, 当责任链创建完成后, 可以调用该方法进行运行
    void handle(int number){
        // 先执行每个责任类需要处理的逻辑
        doHandle(number);
        // 如果还有下一个责任类, 则继续让下一个责任类运行逻辑
        if (nextHandle != null) {
            nextHandle.handle(number);
        }
    }

    // 每个责任类需要重写的方法, 实际处理的方法
    abstract void doHandle(int number);
}

/**
 * 实际处理类
 */
class FirstHandle extends Handle{
    @Override
    void doHandle(int number) {
        if (number < 10) return;
        System.out.println("the First Run!");
    }
}
class SecondHandle extends Handle{
    @Override
    void doHandle(int number) {
        if (number < 100) return;
        System.out.println("the Second Run!");
    }
}
class ThirdHandle extends Handle{
    @Override
    void doHandle(int number) {
        if (number < 50) return;
        System.out.println("the Third Run!");
    }
}

