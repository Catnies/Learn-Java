package top.catnies.designpattern.适配器模式;


/**
 * 旧的接口, 内部有一个 work 方法, 无入参也无返回值.
 */
public interface OldInterface {
    void simpleWork();
}

/**
 * 实现了旧的接口的类.
 */
class Singer implements OldInterface {
    public void simpleWork() {
        System.out.println("Singer work!");
    }
}


/**
 * 新的接口, 内部有一个 work 方法, 但是有一个入参
 */
interface NewInterface {
    void AdvanceWork(String food);
}

/**
 * 新的类, 实现新的接口
 */
class Eater implements NewInterface {
    public void AdvanceWork(String food) {
        System.out.println("Eat Food: " + food);
    }
}


/**
 * 于是, 我们可以创建一个类适配器.
 * 这样, 我们相当于包装了一遍旧的类, 使它实现了新的接口,
 */
class ClassAdapter implements NewInterface {

    private Singer singer;
    public ClassAdapter(Singer singer) {
        this.singer = singer;
    }

    @Override
    public void AdvanceWork(String food) {
        singer.simpleWork();
    }
}

/**
 * 测试代码
 */
class main {
    public static void main(String[] args) {
        // 现在有个方法需要 NewInterface, 而我们想要 Singer 来执行.
        Singer singer = new Singer();
        // 这时候, 我们就可以借助适配器类.
        NewInterface newInterface = new ClassAdapter(singer);
        work(newInterface);
    }

    public static void work(NewInterface newInterface) {
        newInterface.AdvanceWork("Eat");
    }
}