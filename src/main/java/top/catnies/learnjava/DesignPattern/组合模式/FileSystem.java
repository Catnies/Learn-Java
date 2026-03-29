package top.catnies.designpattern.组合模式;

/**
 * 组合模式
 * 将一组对象组织（Compose）成树形结构，以表示一种“部分 - 整体”的层次结构。组合让使用者可以统一单个对象和组合对象的处理逻辑。
 */

import java.util.ArrayList;
import java.util.List;

/**
 * 创建一个每个节点都继承的抽象接口
 * 例如, 此处模拟 文件 和 文件夹 的关系, 文件和文件夹都应该实现 FileSystem 接口.
 * 其中文件夹又可以往里面放入实现了  FileSystem 的文件或者文件夹.
 */
public interface FileSystem {
    void display();
}
/**
 * 定义一个基本的文件类, 也就是叶子节点
 */
class File implements FileSystem{
    private String name;

    public File(String name){
        this.name = name;
    }

    @Override
    public void display() {
        System.out.println("This File: " + name);
    }
}
/**
 * 定义一个文件夹类, 文件夹可以存储文件和文件夹, 也就是枝节点
 */
class Folder implements FileSystem{
    private String name;
    private final List<FileSystem> fileSystems = new ArrayList<>();

    public Folder(String name){
        this.name = name;
    }

    @Override
    public void display() {
        System.out.println("This Folder: " + name);
        fileSystems.forEach(FileSystem::display);
    }

    public void add(FileSystem fileSystem) {
        fileSystems.add(fileSystem);
    }
    public void remove(FileSystem fileSystem) {
        fileSystems.remove(fileSystem);
    }

}

/**
 * 测试代码
 */
class main {
    // 组合模式可以反映一个树形结构, 例如文件管理系统
    public static void main(String[] args) {

        Folder outFolder = new Folder("Out Folder");
        File text = new File("Something.txt");
        outFolder.add(text);
        Folder insideFolder = new Folder("Inside Folder");
        outFolder.add(insideFolder);

        File zip = new File("Himitsu.zip");
        insideFolder.add(zip);

        outFolder.display();

    }
}