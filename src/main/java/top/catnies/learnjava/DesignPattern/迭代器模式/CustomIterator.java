package top.catnies.designpattern.迭代器模式;

import java.util.Arrays;
import java.util.List;

/**
 * 迭代器模式
 * 用于遍历集合对象，而无需暴露该对象的底层表示。
 * 这种模式非常适合在处理大型集合时使用，因为它提供了一种更抽象的方式来访问集合的元素。
 */
class main {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        ListCustomIterator<Integer> customIterator = new ListCustomIterator<>(list);
        while (customIterator.hasNext()) {
            System.out.println(customIterator.next());
        }
    }
}


/**
 * 创建一个迭代器接口以及其实现
 */
interface CustomIterator<E> {
    boolean hasNext();
    E next();
}
/**
 * 实现迭代器接口, 这是一个可以迭代 List 对象的迭代器.
 * 通过构造方法把需要迭代的对象传入迭代器内.
 * @param <E> 迭代的元素类型
 */
class ListCustomIterator<E> implements CustomIterator<E> {
    private final List<E> list;
    private int index;

    public ListCustomIterator(List<E> list) {
        this.list = list;
    }
    public boolean hasNext(){
        if (index < list.size()) {
            return true;
        }
        return false;
    }
    public E next(){
        return list.get(index++);
    }
}


