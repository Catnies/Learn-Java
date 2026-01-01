package top.catnies.learnjava.CollectionFramework;

// 环形数组

/**
  你可以把它记成“循环下标”的通用模板:
  往右走一步（+1） -> idx = (idx + 1) % size;
  往左走一步（-1） -> idx = (idx - 1 + size) % size; 推荐写法（避免负数）
  因为在 Java 里：-1 % size == -1（Java 的 % 对负数会得到负数余数），所以不能直接 (head - 1) % size。
  于是我们先加 size，把它变成非负：
 */
public class MyCycleArray<T> {
    private T[] data;
    private int head;
    private int tail;
    private int count;
    private int size;

    // 创建数组
    @SuppressWarnings("unchecked")
    public MyCycleArray(int size) {
        this.size = size;
        this.data = (T[]) new Object[size];
        // head 指向第一个有效元素的索引，闭区间
        this.head = 0;
        // 切记 tail 是一个开区间，即 tail 指向最后一个有效元素的下一个位置索引
        this.tail = 0;
        this.count = 0;
    }

    // 自动扩缩容辅助函数
    @SuppressWarnings("unchecked")
    private void resize(int newSize) {
        // 创建新的数组
        T[] newArr = (T[]) new Object[newSize];
        // 将旧数组的元素复制到新数组中
        for (int i = 0; i < count; i++) {
            newArr[i] = data[(head + i) % size];
        }
        data = newArr;
        // 重置 start 和 end 指针
        head = 0;
        tail = count;
        size = newSize;
    }

    // 在数组头部添加元素，时间复杂度 O(1)
    public void addFirst(T val) {
        // 当数组满时，扩容为原来的两倍
        if (isFull()) {
            resize(size * 2);
        }
        // 因为 start 是闭区间，所以先左移，再赋值   [_, _, 1]
        head = (size + head - 1) % size;
        data[head] = val;
        count++;
    }

    // 删除数组头部元素，时间复杂度 O(1)
    public T removeFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("Array is empty");
        }
        // 因为 start 是闭区间，所以先赋值，再右移
        T oldValue = data[head];
        data[head] = null;
        head = (head + 1) % size;
        count--;
        // 如果数组元素数量减少到原大小的四分之一，则减小数组大小为一半
        if (count > 0 && count == size / 4) {
            resize(size / 2);
        }
        return oldValue;
    }

    // 在数组尾部添加元素，时间复杂度 O(1)
    public void addLast(T val) {
        if (isFull()) {
            resize(size * 2);
        }
        // 因为 tail 是开区间，所以是先赋值，再右移
        data[tail] = val;
        tail = (tail + 1) % size;
        count++;
    }

    // 删除数组尾部元素，时间复杂度 O(1)
    public T removeLast() {
        if (isEmpty()) {
            throw new IllegalStateException("Array is empty");
        }
        // 因为 end 是开区间，所以先左移，再赋值
        tail = (tail - 1 + size) % size;
        T oldValue = data[tail];
        data[tail] = null;
        count--;
        // 缩容
        if (count > 0 && count == size / 4) {
            resize(size / 2);
        }
        return oldValue;
    }

    // 获取数组头部元素，时间复杂度 O(1)
    public T getFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("Array is empty");
        }
        return data[head];
    }

    // 获取数组尾部元素，时间复杂度 O(1)
    public T getLast() {
        if (isEmpty()) {
            throw new IllegalStateException("Array is empty");
        }
        // end 是开区间，指向的是下一个元素的位置，所以要减 1
        return data[(tail - 1 + size) % size];
    }

    // 插入指定索引的元素
    public void insert(int index, T val) {
        if (index < 0 || index > count) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + count);
        }
        if (isFull()) resize(size * 2);

        // 特殊位置直接复用已有操作
        if (index == 0) { addFirst(val); return; }
        if (index == count) { addLast(val); return; }

        // 选搬, 哪边元素少就搬哪一边
        if (index < count / 2) {
            // 1) head 左移，扩大一格空间
            head = (head - 1 + size) % size;

            // 2) 把原来 [0, index-1] 这一段整体左移一格
            //    注意：现在 head 变了，所以“逻辑 i”对应的物理位置也跟着变
            for (int i = 0; i < index; i++) {
                data[physicalIndex(i)] = data[physicalIndex(i + 1)];
            }

            // 3) 放入新元素（逻辑 index）
            data[physicalIndex(index)] = val;
        } else {
            // 1) tail 右移，扩大一格空间
            //    先把 tail 指向的位置空出来：tail 作为开区间，右移意味着新增一个可写槽
            tail = (tail + 1) % size;

            // 2) 把原来 [index, count-1] 整体右移一格
            for (int i = count; i > index; i--) {
                data[physicalIndex(i)] = data[physicalIndex(i - 1)];
            }

            // 3) 放入新元素
            data[physicalIndex(index)] = val;
        }

        count++;
    }

    // 删除指定索引的元素
    public T remove(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + count);
        }

        if (index == 0) return removeFirst();
        if (index == count - 1) return removeLast();

        int idx = physicalIndex(index);
        T oldValue = data[idx];

        if (index < count / 2) {
            // 把 [0, index-1] 向右挪一格（从后往前避免覆盖）
            for (int i = index; i > 0; i--) {
                data[physicalIndex(i)] = data[physicalIndex(i - 1)];
            }
            // head 右移，丢掉重复的旧头
            data[head] = null;
            head = (head + 1) % size;
        } else {
            // 把 [index+1, count-1] 向左挪一格
            for (int i = index; i < count - 1; i++) {
                data[physicalIndex(i)] = data[physicalIndex(i + 1)];
            }
            // tail 左移，丢掉重复的旧尾（tail 是开区间）
            tail = (tail - 1 + size) % size;
            data[tail] = null;
        }

        count--;

        if (count > 0 && count == size / 4) {
            resize(size / 2);
        }

        return oldValue;
    }


    // 获取逻辑下下标, 第i个元素的位置就是 head + i;
    private int physicalIndex(int index) {
        return (head + index) % size;
    }

    public boolean isFull() {
        return count == size;
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }
}
