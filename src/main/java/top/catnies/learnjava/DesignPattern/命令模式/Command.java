package top.catnies.designpattern.命令模式;

import java.util.Stack;

/**
 * 命令模式
 * 将请求封装为一个对象，从而使得调用请求的人与处理请求的对象解耦。这使得我们可以将具体的请求、调用者和接收者进行更灵活的组合。
 * 命令模式常用于实现任务队列、历史记录（如撤销、重做操作）等场景。
 */
public class Command {
    public static void main(String[] args) {
        Receiver computer = new Computer();
        Controller controller = new Controller();
        controller.execute(new OpenCommand(computer));
        controller.execute(new CloseCommand(computer));
        controller.undo();
        controller.undo();
    }
}


/**
 * 定义接收者类, 每个接收者都有不同的指令
 * 例如当前场景是使用遥控器控制电器的开关, 每个电器都是可以被命令操作的对象, 都具有 开 和 关 两种功能.
 */
interface Receiver{
    void turnOn();
    void turnOff();
}
class Computer implements Receiver {
    public void turnOn() {
        System.out.println("Computer is turned on!");
    }
    public void turnOff() {
        System.out.println("Computer is turned off!");
    }
    public void runCode(){
        System.out.println("Computer is running!");
    }
}


/**
 * 定义命令类, 每个命令类都需要实现该类, 每个命令都有一个执行 和 撤销的方法
 * 例如, 我们有 开 和 关 两种命令.
 */
interface CommandInterface {
    void execute();
    void undo();
}
class OpenCommand implements CommandInterface {
    private final Receiver receiver;
    public OpenCommand(Receiver receiver) {
        this.receiver = receiver;
    }
    @Override
    public void execute() {
        receiver.turnOn();
    }
    @Override
    public void undo() {
        System.out.println("Undo: OpenCommand");
    }

}
class CloseCommand implements CommandInterface {
    private final Receiver receiver;
    public CloseCommand(Receiver receiver) {
        this.receiver = receiver;
    }
    @Override
    public void execute() {
        receiver.turnOff();
    }
    @Override
    public void undo() {
        System.out.println("Undo: CloseCommand");
    }
}
// 针对电脑的功能做的独有的命令
class RunCommand implements CommandInterface {
    private final Computer computer;
    public RunCommand(Computer computer) {
        this.computer = computer;
    }
    @Override
    public void execute() {
        computer.runCode();
    }
    @Override
    public void undo() {
        System.out.println("Undo: RunCommand");
    }
}

/**
 * 遥控器类, 用于控制电器.
 * 当需要操作电器时, 应该新建一个遥控器类 + 一个命令类.
 * 然后把电器传递给命令, 命令传递给遥控器.
 */
class Controller {
    private final Stack<CommandInterface> commands;
    public Controller() {
        this.commands = new Stack<>();
    }
    public void execute(CommandInterface command) {
        command.execute();
        commands.push(command);
    }
    public void undo() {
        if (commands.isEmpty()) return;
        CommandInterface pop = commands.pop();
        pop.undo();
    }
}