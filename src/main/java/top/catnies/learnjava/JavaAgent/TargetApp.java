package top.catnies.learnjava.JavaAgent;

import java.lang.management.ManagementFactory;

public class TargetApp {

    public static void main(String[] args) throws InterruptedException {
        // 打印一下自己的进程ID (PID)，方便你待会儿找到它
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        System.out.println("目标程序正在运行！我的进程ID (PID) 是: " + pid);

        while (true) {
            System.out.println("我还在跑... (等待被Agent注入)");
            Thread.sleep(5000); // 每5秒打印一次
        }
    }

}
