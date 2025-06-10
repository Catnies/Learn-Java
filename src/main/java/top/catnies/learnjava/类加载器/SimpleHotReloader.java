package top.catnies.learnjava.类加载器;


import java.lang.reflect.InvocationTargetException;

// 热重载简单实现
public class SimpleHotReloader {
    public SimpleHotClassLoader currentLoader;
    public Object currentInstance;
    public String className;


    public SimpleHotReloader(String className) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        this.className = className;
        reload();
    }

    // 核心的热重载方法
    public void reload() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // 1. 关闭旧的类加载器.
        if (currentLoader != null) {
            currentLoader = null;
        }

        // 2. 创建新的类加载器
        currentLoader = new SimpleHotClassLoader();

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

}
