package top.catnies.learnjava.JavaAgent;

import com.sun.tools.attach.VirtualMachine;

public class AttachTool {

    // dongtai1
    public static void main(String[] args) {
        // 这里填刚才 TargetApp 打印出来的 PID
        String targetPid = "29284"; // <--- 记得改成你实际运行时的PID！！

        // 这里填 SimpleAgent.jar 的绝对路径 (Windows下路径要转义，或者用 /)
        // 假设 jar 包在 D盘根目录，你自己改一下路径
        String agentPath = "F:\\IDEA Projects\\StudyProjects\\Learn-Java\\src\\main\\java\\SimpleAgent.jar";

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
