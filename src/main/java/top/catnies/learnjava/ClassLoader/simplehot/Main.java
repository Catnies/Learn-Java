package top.catnies.learnjava.ClassLoader.simplehot;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        testSimpleHotReloader();
    }

    // 测试热重载.
    public static void testSimpleHotReloader() throws Exception {
        // 1. 初始化加载类.
        SimpleHotReloader hotReloader = new SimpleHotReloader("top.catnies.learnjava.ClassLoader.Resource", "top.catnies");
        Object currentInstance = hotReloader.getCurrentInstance();
        System.out.println(currentInstance);

        // 2. 等待手动替换完class
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        // 3. 重新加载类.
        hotReloader.reload();
        System.out.println(hotReloader.getCurrentInstance());
    }

}
