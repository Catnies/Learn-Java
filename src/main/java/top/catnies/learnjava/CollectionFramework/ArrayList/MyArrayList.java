package top.catnies.learnjava.CollectionFramework.ArrayList;

import org.jetbrains.annotations.NotNull;

import java.util.*;

// 使用数组手动实现一个 ArrayList
public class MyArrayList<E> implements Iterable<E> {

    // 测试方法
    public static void main(String[] args) {
        MyArrayList<Integer> list = new MyArrayList<>(2);

        System.out.println("=== 基本 add / get / size ===");
        list.add(1);
        list.add(2);
        list.add(null); // 触发扩容（2 -> 4）
        System.out.println("size = " + list.size()); // 期望 3

        list.add(null);
        System.out.println("size = " + list.size());
    }

    // 底层数据
    private E[] data;
    // 当前尺寸
    private int size;
    // 默认大小
    private static final int DEFAULT_CAPACITY = 10;


    // 根据容量进行初始化
    public MyArrayList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        }
        if (capacity == 0) {
            capacity = 1;
        }
        this.data = (E[]) new Object[capacity];
    }

    // 使用默认大小初始化
    public MyArrayList() {
        this(DEFAULT_CAPACITY);
    }

    // 数组长度
    public int size() {
        return size;
    }

    // 是否为空数组
    public boolean isEmpty() {
        return size == 0;
    }

    // 返回数组
    public @NotNull E[] toArray() {
        return Arrays.copyOf(data, size);
    }

    // 增
    public boolean add(E o) {
        // 先检查数组容量是否满, 如果满了则扩容
        if (size == data.length) {
            this.resize(data.length * 2);
        }
        // 在数组末尾添加元素
        data[size++] = (E) o;
        return true;
    }

    // 插入
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        // 自动扩容
        if (size == data.length) {
            this.resize(data.length * 2);
        }
        // 从后往前遍历, 依次把前一个元素复制到后一个位置
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        // 最后在目标位置填充元素
        data[index] = (E) element;
        size++;
    }

    // 删
    public boolean remove(E o) {
        // 先找到目标元素的索引
        for (int i = 0; i < size; i++) {
            if (Objects.equals(data[i], o)) {
                // 找到索引后, 调用索引移除
                remove(i);
                return true;
            }
        }
        return false;
    }

    // 根据索引移除
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        // 暂存需要被移除的元素
        E oldValue = data[index];
        // 从目标元素开始, 往后遍历, 依次把后一个元素复制到当前位置
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        // 最后删除残留的元素, 减少数组大小
        data[size - 1] = null;
        size--;
        // 自动缩容
        if (size <= data.length / 4) {
            resize(data.length / 2);
        }
        return oldValue;
    }

    // 删除所有
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
        resize(DEFAULT_CAPACITY);
    }

    // 改
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E oldValue = data[index];
        data[index] = (E) element;
        return oldValue;
    }

    // 查询
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return data[index];
    }

    // 是否包含某个值
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // 根据元素查询第一次出现的索引
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(data[i], o)) {
                return i;
            }
        }
        return -1;
    }

    // 根据元素查询最后一次出现的索引
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(data[i], o)) {
                return i;
            }
        }
        return -1;
    }

    // 内部方法: 重新调整数组大小
    private void resize(int newCapacity) {
        if (newCapacity <= DEFAULT_CAPACITY) newCapacity = DEFAULT_CAPACITY;
        E[] newArray = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = data[i];
        }
        this.data = newArray;
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return new Iterator<>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {throw new NoSuchElementException();}
                return data[cursor++];
            }
        };
    }
}
