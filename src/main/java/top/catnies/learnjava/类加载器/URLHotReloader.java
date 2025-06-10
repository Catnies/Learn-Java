package top.catnies.learnjava.类加载器;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class URLHotReloader {
    public URLClassLoader currentLoader;
    public Object currentInstance;
    public String className;
    public String classPath;

    public URLHotReloader(String className, String classPath) throws Exception {
        this.className = className;
        this.classPath = classPath;
        reload();
    }

    // 核心的热重载方法
    public void reload() throws Exception {
        // 1. 关闭旧的类加载器.
        if (currentLoader != null) {
            currentLoader.close();
            currentLoader = null;
            currentInstance = null;
        }

        // 2. 创建新的类加载器
        currentLoader = new URLHotClassLoader(new URL[]{new File(classPath).toURI().toURL()}); // 注意：这里需要设置null隔离父类加载器, 否则Resource类会无法加载, 因为父加载器在启动时已经加载了.

        // 3. 重新加载类
        Class<?> clazz = currentLoader.loadClass(className);
        currentInstance = clazz.getDeclaredConstructor().newInstance();

        // 4. 类热加载完成
        System.out.println("class reload success!");
    }

    // 获取当前实例
    public Object getCurrentInstance() {
        return currentInstance;
    }


    // 添加一个验证方法
    public void validateSetup() throws Exception {
        String expectedClassFile = className.replace('.', '/') + ".class";
        File classFile = new File(classPath, expectedClassFile);

        System.out.println("期望的class文件位置: " + classFile.getAbsolutePath());
        System.out.println("文件是否存在: " + classFile.exists());

        if (!classFile.exists()) {
            throw new Exception("类文件不存在: " + classFile.getAbsolutePath());
        }
    }

}
