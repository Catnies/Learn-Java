package top.catnies.learnjava.ClassLoader.simplehot;

import java.io.InputStream;

public class SimpleHotClassLoader extends ClassLoader {

    private String allowPackage;


    public SimpleHotClassLoader(String allowPackage) {
        this.allowPackage = allowPackage;
    }


    // 加载类
    @Override
    public Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(className)) {
            // 检查类是否已经被加载.
            Class<?> c = findLoadedClass(className);
            if (c == null) {
                // 如果是我们自己需要加载的类, 就让我们自己来找.
                if (className.startsWith(allowPackage)) {
                    c = findClass(className);
                } else {
                    // 否则就交给父加载器
                    c = getParent().loadClass(className);
                }
            }
            // 可以手动指定是否需要解析类
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }


    // 寻找类并解析成class.
    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        try {
            // 获取class文件路径, 读取class文件, 获取其二进制输入流.
            String path = className.replace('.', '/').concat(".class");
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            // 调用defineClass方法, 将其转换为Class对象.
            return defineClass(className, bytes, 0, bytes.length);
        } catch (Exception e) {
            throw new NoClassDefFoundError(className + " not found");
        }
    }
}
