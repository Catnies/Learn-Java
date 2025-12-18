package top.catnies.learnjava.CollectionFramework.LinkedList;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class MyLinkedList<E> implements Iterable<E>{

    // 当前尺寸
    private int size;
    // 头节点
    private Node<E> head;
    // 尾节点
    private Node<E> tail;

    // 数组长度
    public int size() {
        return size;
    }

    // 是否为空数组
    public boolean isEmpty() {
        return size == 0;
    }

    // 增 - 头插
    public void linkFirst(E o) {
        // 新建节点
        Node<E> node = new Node<>(null, o, head);
        // 如果头部节点不为空, 则衔接
        if (head != null) {
            head.prev = node;
        }
        // 如果尾节点为空, 说明头尾都没数据, 是空的, 则把头节点设置为当前节点
        else {
            tail = node;
        }
        // 设置头节点
        head = node;
        size++;
    }

    // 增 - 尾插
    public void linkLast(E o) {
        // 新建节点
        Node<E> node = new Node<>(tail, o, null);
        // 如果尾节点不为空, 则将自己衔接到尾节点
        if (tail != null) {
            tail.next = node;
        }
        // 如果尾节点为空, 说明头尾都没数据, 是空的, 则把头节点设置为当前节点
        else {
            head = node;
        }
        // 设置尾节点为自己
        tail = node;
        size++;
    }

    // 插入 0 x -> 1 2 3
    public void insert(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        // 尾插
        if (index == size) {
            linkLast(element);
            return;
        }
        // 头插
        if (index == 0) {
            Node<E> newNode = new Node<>(null, element, head);
            if (head == null) tail = newNode;
            else head.prev = newNode;
            head = newNode;
            size++;
            return;
        }
        // 先找到需要插入的位置的节点
        Node<E> targetNode = head;
        for (int i = 0; i < index; i++) targetNode = targetNode.next;
        // 然后把我们的节点插入在他之前
        Node<E> newNode = new Node<>(targetNode.prev, element, targetNode);
        targetNode.prev.next = newNode;
        targetNode.prev = newNode;
        size++;
    }

    // 删
    public boolean remove(E o) {
        if (head == null) return false;
        // 从头节点开始寻找
        Node<E> cur = head;
        while (cur != null) {
            if (Objects.equals(cur.data, o)) {
                // 如果当前节点既是头也是尾
                if (cur == head && cur == tail) {
                    clear();
                    return true;
                }
                // 如果当前节点是头节点, 那么就提取下一个节点做头节点
                if (cur == head) {
                    head = head.next;
                    head.prev = null;
                    size--;
                    return true;
                }
                // 如果当前节点是尾节点, 那么就提取上一个节点做尾节点
                if (cur == tail) {
                    tail = tail.prev;
                    tail.next = null;
                    size--;
                    return true;
                }
                // 如果当前节点位于中间, 那么就删除中间的
                cur.prev.next = cur.next;
                cur.next.prev = cur.prev;
                size--;
                return true;
            }
            // 没找到就把自己设置为下一个节点
            cur = cur.next;
        }
        return false;
    }

    // 根据索引移除
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        // 删除全部
        if (index == 0 && size == 1) {
            E oldValue = head.data;
            clear();
            return oldValue;
        }
        // 尾删
        if (index == size - 1) {
            E oldValue = tail.data;
            tail = tail.prev;
            tail.next = null;
            size--;
            return oldValue;
        }
        // 头删
        if (index == 0) {
            E oldValue = head.data;
            head = head.next;
            head.prev = null;
            size--;
            return oldValue;
        }
        // 先找到目标索引的 Node
        Node<E> cur = head;
        for (int i = 0; i < index; i++) cur = cur.next;
        // 然后将其前面和后面的相接
        cur.prev.next = cur.next;
        cur.next.prev = cur.prev;
        size--;
        return cur.data;
    }

    // 删除所有
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    // 改
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        // 先找到节点
        Node<E> cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        E oldValue = cur.data;
        cur.data = element;
        return oldValue;
    }

    // 查询
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur.data;
    }

    // 是否包含某个值
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // 根据元素查询第一次出现的索引
    public int indexOf(Object o) {
        if (head == null) return -1;
        Node<E> cur = head;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(cur.data, o)) {
                return i;
            }
            cur = cur.next;
        }
        return -1;
    }

    // 根据元素查询最后一次出现的索引
    public int lastIndexOf(Object o) {
        if (head == null) return -1;
        Node<E> cur = tail;
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(cur.data, o)) {
                return i;
            }
            cur = cur.prev;
        }
        return -1;
    }

    // 获取Node
    private Node<E> node(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        // 根据 index 决定是从头或者尾遍历
        if (index <= size / 2) {
            Node<E> cur = head;
            for (int i = 0; i < index; i++) cur = cur.next;
            return cur;
        } else {
            Node<E> cur = tail;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
            return cur;
        }
    }

    // 删除指定节点
    private E unlink(Node<E> target) {
        E data = target.data;
        Node<E> prev = target.prev;
        Node<E> next = target.next;

        // 如果 target 是头, 进行头删
        if (prev == null) head = next;
        // 否则正常将上一个节点的 next 拼接到 target 的下一个
        else prev.next = next;

        // 如果 target 是尾, 进行尾删
        if (next == null) tail = prev;
        // 否则正常将下一个节点的 pre 拼接到 target 的上一个
        else next.prev = prev;

        size--;
        return data;
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> cur = head;

            @Override
            public boolean hasNext() {
                return cur.next != null;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E val = cur.data;
                cur = cur.next;
                return val;
            }
        };
    }

    class Node<E>{
        E data;
        Node<E> prev;
        Node<E> next;

        public Node(E data) {
            this.data = data;
        }

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

    }
}
