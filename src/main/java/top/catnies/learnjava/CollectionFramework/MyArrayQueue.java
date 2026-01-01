package top.catnies.learnjava.CollectionFramework;

// 用环形数组实现的双向队列, 所有操作复杂度都是O(1)
public class MyArrayQueue<E> {
    MyCycleArray<E> arr = new MyCycleArray<>(10);

    // 从队头插入元素，时间复杂度 O(1)
    public void addFirst(E e) {
        arr.addFirst(e);
    }

    // 从队尾插入元素，时间复杂度 O(1)
    public void addLast(E e) {
        arr.addLast(e);
    }

    // 从队头删除元素，时间复杂度 O(1)
    public E removeFirst() {
        return arr.removeFirst();
    }

    // 从队尾删除元素，时间复杂度 O(1)
    public E removeLast() {
        return arr.removeLast();
    }

    // 查看队头元素，时间复杂度 O(1)
    public E peekFirst() {
        return arr.getFirst();
    }

    // 查看队尾元素，时间复杂度 O(1)
    public E peekLast() {
        return arr.getLast();
    }

}
