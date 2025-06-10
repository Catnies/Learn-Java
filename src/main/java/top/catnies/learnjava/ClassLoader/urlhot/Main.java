package top.catnies.learnjava.ClassLoader.urlhot;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        testURLHotReloader();
    }


    // 测试热重载.
    public static void testURLHotReloader() throws Exception {
        // 1. 初始化加载类.
        URLHotReloader hotReloader = new URLHotReloader(
                "top.catnies.learnjava.ClassLoader.Resource",  // 完整的类名
                 Main.class.getClassLoader().getResource("")// 根目录
        );

        // 2. 验证设置是否正确
        hotReloader.validateSetup();

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
