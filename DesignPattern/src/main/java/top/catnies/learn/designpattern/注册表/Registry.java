package top.catnies.learn.designpattern.注册表;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Registry<T> {

    private Map<String, Holder<T>> valueByName = new HashMap<>();
    private Map<T, String> idByValue = new IdentityHashMap<>();
    private List<T> valueByIndex = new ArrayList<>();
    private List<Holder<T>> holderByIndex = new ArrayList<>();
    private Map<T, Integer> indexByValue = new IdentityHashMap<>();

    // 注册
    public void register(String name, T value) {
        // 先检查是否已经注册, 如果已经注册了, 就重绑定;
        Holder<T> oldHolder = holderByName(name);
        if (oldHolder != null) {
            T oldValue = oldHolder.value;
            idByValue.remove(oldValue);
            indexByValue.remove(oldValue);

            valueByIndex.set(oldHolder.index, value);

            oldHolder.setValue(value);
            idByValue.put(value, name);
            indexByValue.put(value, oldHolder.index);
            return;
        }

        int index = valueByIndex.size();
        indexByValue.put(value, index);
        valueByIndex.add(value);
        idByValue.put(value, name);

        Holder<T> holder = new Holder<>(name, index) {{
            setValue(value);
        }};
        holderByIndex.add(holder);
        valueByName.put(name, holder);
    }

    public Holder<T> holderByName(String name) {
        return valueByName.get(name);
    }

    public T valueByName(String name) {
        Holder<T> holder = valueByName.get(name);
        if (holder == null) {
            throw new IllegalArgumentException("No such name: " + name);
        }
        return holder.getValue();
    }


    public T valueByIndex(int index) {
        return valueByIndex.get(index);
    }

    public Integer indexByValue(T value) {
        return indexByValue.get(value);
    }

    static class Holder<T> {
        int index;
        String name;
        @Setter
        @Getter
        T value;

        public Holder(String name, int index) {
            this.name = name;
            this.index = index;
        }

    }

}
