package top.catnies.learnjava.CollectionFramework;

// 用数组实现的栈
// 我们规定数组末尾为栈顶, 这样所有操作都是操作数组末尾, 永远都是 O(1) 的复杂度;
public class MyArrayStack<E> {
    private MyArrayList<E> arr = new MyArrayList<>();

    // 向栈顶加入元素，时间复杂度 O(1)
    public void push(E e) {
        arr.add(e);
    }

    // 从栈顶弹出元素，时间复杂度 O(1)
    public E pop() {
        return arr.remove(arr.size() - 1);
    }

    // 查看栈顶元素，时间复杂度 O(1)
    public E peek() {
        return arr.get(arr.size() - 1);
    }

    // 返回栈中的元素个数，时间复杂度 O(1)
    public int size() {
        return arr.size();
    }
}
