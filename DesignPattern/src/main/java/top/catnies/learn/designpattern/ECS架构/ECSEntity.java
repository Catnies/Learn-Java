package top.catnies.learn.designpattern.ECS架构;


import lombok.Getter;

/**
 * ECS 中的 Entity（实体）。
 * <p>
 * 实体本身尽量保持“空壳”状态，它不直接保存业务数据，
 * 只负责提供一个稳定的唯一 ID，真正的数据放在 Component（组件）里。
 * 这样做的好处是：
 * 1. 实体很轻量，创建和销毁成本低。
 * 2. 数据和行为解耦，方便系统按需批量处理。
 * 3. 不同系统只需要关心“拥有某些组件的实体”，而不是关心实体类型继承关系。
 */
public class ECSEntity {
    /**
     * 用一个自增序列模拟实体 ID 生成器。
     * 教学示例里这样足够直观，真实项目中也可以换成 UUID 或对象池方案。
     */
    private static int nextId = 0;

    @Getter
    private final int id;

    /**
     * 创建实体时自动分配一个唯一 ID。
     */
    public ECSEntity() {
        id = nextId++;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ECSEntity entity = (ECSEntity) obj;
        return id == entity.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
