package top.catnies.designpattern.外观模式;

/*外观设计模式
* 我们现在需要执行MethodA B C,正常情况我们只能一个一个执行
* 但是有了外观模式之后，我们只需要调用facade的execute方法，就能执行MethodA B C
* 这样就能简化我们的操作，提高效率.
*
* 例如: 我们需要启动 / 重载 / 关闭某个功能的时候, 我们需要内部的模块分个按照特定的顺序进行执行刷新数据
* 我们这样可以把整个逻辑包装成一个新的入口.
* */
public class Facade {
    /*首先我们需要将需要执行的MethodA B C实例化*/
    private MethodA methodA;
    private MethodB methodB;
    private MethodC methodC;


    /*然后我们需要定义一个构造方法，传入MethodA B C实例化*/
    public Facade(MethodA methodA, MethodB methodB, MethodC methodC) {
        this.methodA = methodA;
        this.methodB = methodB;
        this.methodC = methodC;
    }

    /*然后我们定义一个execute方法，调用MethodA B C的execute方法*/
    public void execute() {
        methodA.methodA();
        methodB.MethodB();
        methodC.MethodC();
    }


    public class MethodA {
        public void methodA() {
            System.out.println("MethodA");
        }
    }

    public class MethodB {
        public void MethodB() {
            System.out.println("MethodB");
        }
    }

    public class MethodC {
        public void MethodC() {
            System.out.println("MethodC");
        }
    }

}
