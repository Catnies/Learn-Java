package top.catnies.designpattern.ECS架构;


import lombok.Getter;

/**
 * ECS架构中的实体,
 */
public class ECSEntity {
    private static int nextId = 0;
    @Getter
    private final int id;

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
