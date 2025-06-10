package top.catnies.learnjava.类加载器;

import java.io.InputStream;

public class SimpleHotClassLoader extends ClassLoader {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            // 1. 只处理特定类（跳过核心类）
            if (name.startsWith("top.catnies.learnjava.类加载器")) {
                // 2. 读取class文件, 获取其二进制输入流.
                String path = name.replace('.', '/') + ".class";
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
                System.out.println("6666");
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                // 3. 调用defineClass方法, 将其转换为Class对象.
                return defineClass(name, bytes, 0, bytes.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 4. 其他类走双亲委派.
        return super.loadClass(name);
    }

}
