package top.catnies.learnjava.类加载器;

// Java类加载器是JVM中负责动态加载类文件到内存中的核心组件。
// 类加载器主要包括：
// Bootstrap ClassLoader（引导类加载器） -> 用原生代码实现，在Java中表现为null，负责加载核心Java API类（如java.lang.*）。
// Extension ClassLoader（扩展类加载器） -> 加载标准扩展API和平台特定的类， 位于$JAVA_HOME/lib/ext目录。
// Application ClassLoader（应用类加载器） -> 加载应用程序类路径上的类，默认的类加载器，处理CLASSPATH环境变量中的类，作为自定义类加载器的默认父加载器。
// 它们从上到下，是组合而非继承关系.

// 类的生命周期：
// 1. 加载：从各种来源（文件系统、网络、数据库）获取.class文件的二进制数据, 在方法区创建Class对象的内部表示, 建立类名到Class对象的映射关系.
// 2. 验证：确保字节码结构正确且安全，防止恶意代码破坏JVM。
// 3. 准备：为类的静态字段分配内存并设置默认值。
// 4. 解析：将符号引用转换为直接引用（可选，可延迟到首次使用时）。
// 5. 初始化：执行类的静态初始化块和静态字段的显式初始化。

// 双亲委派模型是为了确保类的一致性设计的, 因为使用双亲委派模型, 一个限定名的类永远只会有一个类加载器负责加载它.
// 双亲委派模型：
// 1. 当一个类需要被加载时, 类加载器首先会将类交给其父类加载器进行加载, 直到父类加载器为null(即顶层的Bootstrap ClassLoader).
// 2. 当类传递到顶层的Bootstrap ClassLoader时, 类才真正开始加载, Bootstrap ClassLoader会首先自己尝试加载, 若加载失败则向下传递给其子加载器进行加载, 直到加载成功.
// 3. 如果所有的类加载器都无法加载成功, 则抛出ClassNotFoundException异常.

import java.io.InputStream;
import java.net.URL;

// 尝试编写一个打破了双亲委派模型的类加载器.
public class DynamicClassLoader {
    public static void main(String[] args) {
        overrideLoadClass();
    }

    public static void getRsPath() {
        URL classResource = DynamicClassLoader.class.getResource("");
        System.out.println("使用 class.getResource 拿到的是当前calss的包路径: " + classResource.getPath()); // ..Learn-Java/build/classes/java/main/top/catnies/learnjava/类加载器
        URL classLoaderResource = DynamicClassLoader.class.getClassLoader().getResource("");
        System.out.println("使用 class.getClassLoader.getResource 拿到的是根路径: " + classLoaderResource.getPath()); // ..Learn-Java/build/resources/main/
    }



    // 自定义类加载器（破坏双亲委派）
    public static void overrideLoadClass() {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                // 1. 只处理特定类（跳过核心类）
                if (name.startsWith("top.catnies.learnjava.类加载器")) {
                    String path = name.replace('.', '/') + ".class";
                    try {
                    // 2. 读取class文件, 获取其二进制输入流.
                    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
                    byte[] bytes = new byte[inputStream.available()];
                    inputStream.read(bytes);
                    // 3. 调用defineClass方法, 将其转换为Class对象.
                    return defineClass(name, bytes, 0, bytes.length);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
                 // 4. 其他类仍走双亲委派
                else {
                    return super.loadClass(name, resolve);
                }
            }
        };


        try {
            // 使用自定义加载器加载类
            Class<?> clazz = classLoader.loadClass("top.catnies.learnjava.类加载器.Resource");
            Object catnies = clazz.getDeclaredConstructor(String.class).newInstance("catnies666");

            // 验证实例类型（注意：因加载器不同，instanceof会返回false）
            if (catnies instanceof top.catnies.learnjava.类加载器.Resource) {
                System.out.println("instance is Resource"); // 实际不会执行
            } else {
                System.out.println("instance is NOT Resource (因不同类加载器)");
            }

            // 正确验证方式：通过类对象比较
            System.out.println("类对象是否相同: " +
                (clazz == top.catnies.learnjava.类加载器.Resource.class)); // 输出false

            // 输出实例对象看看内容
            System.out.println(catnies);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    // 不破坏双亲委派
    public static void overrideFindClass() {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                // 1. 只处理特定类（跳过核心类）
                if (name.startsWith("top.catnies.learnjava.类加载器")) {
                    String path = name.replace('.', '/') + ".class";
                    try {
                        // 2. 读取class文件, 获取其二进制输入流.
                        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
                        byte[] bytes = new byte[inputStream.available()];
                        inputStream.read(bytes);
                       // 3. 调用defineClass方法, 将其转换为Class对象.
                        return defineClass(name, bytes, 0, bytes.length);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                } else {
                    return super.findClass(name);
                }
            }
        };

        try {
            // 使用自定义加载器加载类
            Class<?> clazz = classLoader.loadClass("top.catnies.learnjava.类加载器.Resource");
            Object catnies = clazz.getDeclaredConstructor(String.class).newInstance("catnies888");

            // 验证实例类型（注意：因加载器不同，instanceof会返回false）
            if (catnies instanceof top.catnies.learnjava.类加载器.Resource) {
                System.out.println("instance is Resource");
            } else {
                System.out.println("instance is NOT Resource ");
            }

            // 正确验证方式：通过类对象比较
            System.out.println("类对象是否相同: " +
                (clazz == top.catnies.learnjava.类加载器.Resource.class));

            // 输出实例对象看看内容
            System.out.println(catnies);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
