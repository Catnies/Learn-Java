package top.catnies.learn.agent;

import com.sun.tools.attach.VirtualMachine;

import java.nio.file.Path;

public class AttachTool {

    // dongtai1
    public static void main(String[] args) {
        // 这里填刚才 TargetApp 打印出来的 PID
        String targetPid = "29284"; // <--- 记得改成你实际运行时的PID！！

        // 多模块拆分后，Agent 包默认位于 JavaAgent 子模块的构建产物目录下。
        String agentPath = Path.of("JavaAgent", "build", "libs", "JavaAgent-1.0-SNAPSHOT.jar")
                .toAbsolutePath()
                .toString();

        try {
            // 1. 连接到目标虚拟机
            VirtualMachine vm = VirtualMachine.attach(targetPid);

            // 2. 加载 Agent
            System.out.println("开始注入 Agent...");
            vm.loadAgent(agentPath);

            // 3. 断开连接
            vm.detach();
            System.out.println("注入完成！快去看 TargetApp 的控制台！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
