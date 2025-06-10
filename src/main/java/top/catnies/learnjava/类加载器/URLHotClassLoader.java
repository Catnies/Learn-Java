package top.catnies.learnjava.类加载器;

import java.net.URL;
import java.net.URLClassLoader;

public class URLHotClassLoader extends URLClassLoader {

    public URLHotClassLoader(URL[] urls) {
        // 🔑 关键：设置父类加载器为null，完全隔离
        super(urls);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // 如果是我们要热重载的类，直接自己加载，不问父亲
        if (name.equals("top.catnies.learnjava.类加载器.Resource")) {
            return findClass(name);
        }

        // 其他类还是使用正常的委派模型
        // 但是我们需要手动处理Java核心类
        if (name.startsWith("java.") || name.startsWith("javax.")) {
            return ClassLoader.getSystemClassLoader().loadClass(name);
        }

        try {
            return findClass(name);
        } catch (ClassNotFoundException e) {
            return ClassLoader.getSystemClassLoader().loadClass(name);
        }
    }
}
