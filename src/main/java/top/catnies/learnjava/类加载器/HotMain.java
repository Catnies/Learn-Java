package top.catnies.learnjava.类加载器;

import java.util.Scanner;


// 热重载简单实现
public class HotMain {

    public static void main(String[] args) throws Exception {
        testURLHotReloader();
    }


    // 测试热重载.
    public static void testSimpleHotReloader() throws Exception {
        // 1. 初始化加载类.
        SimpleHotReloader hotReloader = new SimpleHotReloader("top.catnies.learnjava.类加载器.Resource");
        Object currentInstance = hotReloader.getCurrentInstance();
        System.out.println(currentInstance);

        // 2. 等待手动替换完class
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        // 3. 重新加载类.
        hotReloader.reload();
        System.out.println(hotReloader.getCurrentInstance());
    }


    // 测试热重载.
    public static void testURLHotReloader() throws Exception {
        // 1. 初始化加载类.
        URLHotReloader hotReloader = new URLHotReloader(
                "top.catnies.learnjava.类加载器.Resource",  // 完整的类名
                "F:/IDEA Projects/StudyProjects/Learn-Java/build/classes/java/main"  // 根目录
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
