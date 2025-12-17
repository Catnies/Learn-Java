package top.catnies.learnjava.JavaAgent;

import java.lang.instrument.Instrumentation;

public class SimpleAgent {

    // 启动时调用 (-javaagent:xxx.jar)
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("【启动时】SimpleAgent premain 运行了!");
    }

    // 运行时附加调用 (VirtualMachine.attach)
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("【运行时】SimpleAgent agentmain 突然袭击!");
    }

}


/*
 * 告诉JVM哪个类是Agent入口的方法，写个文本文件，名字就叫MANIFEST.MF
 * Premain-Class: top.catnies.learnjava.JavaAgent.SimpleAgent
 * Can-Redefine-Classes: true
 *
 * 然后编译文件, 并且打包
 * 1. javac SimpleAgent.java MainApp.java
 * 2. jar cvfm SimpleAgent.jar MANIFEST.MF SimpleAgent.class MainApp.class
 *
 * 接下来用命令行启动你的主程序，并指定Agent包：
 *
 */
