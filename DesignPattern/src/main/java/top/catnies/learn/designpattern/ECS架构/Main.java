package top.catnies.learn.designpattern.ECS架构;


import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * ECS架构:
 * ECS代表 Entity-Component-System（实体-组件-系统）
 * Entity（实体）：      一个空的容器或者标签，比如游戏中的 "玩家"、"敌人"、"子弹"
 * Component（组件）：   每个标签组件代表一种特定的功能，比如 "位置"、"血量"、"移动速度" 等
 * System（系统）：      专门处理具有特定组件组合的实体.
 * <p>
 * 这个示例故意做得很小，只保留 ECS 最核心的三层：
 * 1. Entity: 只保留 ID，不承载业务逻辑。
 * 2. Component: 纯数据对象，描述“实体拥有什么属性”。
 * 3. System: 按组件组合批量处理实体，描述“实体应该发生什么行为”。
 */
public class Main {

    public static void main(String[] args) {
        // 世界对象负责统一管理实体和组件仓库。
        World world = new World();

        // 创建一个玩家实体，并挂上它需要的组件。
        ECSEntity player = world.createEntity();
        world.addComponent(player, new NameComponent("玩家"));
        world.addComponent(player, new PositionComponent(0, 0));
        world.addComponent(player, new VelocityComponent(1, 1));
        world.addComponent(player, new HealthComponent(100));

        // 创建一个怪物实体，它也有位置和血量，但移动速度不同。
        ECSEntity monster = world.createEntity();
        world.addComponent(monster, new NameComponent("史莱姆"));
        world.addComponent(monster, new PositionComponent(5, 2));
        world.addComponent(monster, new VelocityComponent(-1, 0));
        world.addComponent(monster, new HealthComponent(40));

        // 系统只关心“哪些实体拥有我需要的组件”，并不关心实体是什么类型。
        MovementSystem movementSystem = new MovementSystem();
        DamageSystem damageSystem = new DamageSystem();
        RenderSystem renderSystem = new RenderSystem();

        System.out.println("=== 初始状态 ===");
        renderSystem.update(world);

        System.out.println("\n=== 第 1 帧：执行移动系统 ===");
        movementSystem.update(world);
        renderSystem.update(world);

        System.out.println("\n=== 第 2 帧：执行受伤系统 ===");
        damageSystem.damage(world, monster, 15);
        damageSystem.damage(world, player, 5);
        renderSystem.update(world);

        System.out.println("\n=== 第 3 帧：继续移动 ===");
        movementSystem.update(world);
        renderSystem.update(world);
    }

    /**
     * 所有组件的标记接口。
     * 组件本身最好只保存数据，不写复杂业务逻辑。
     */
    interface Component {
    }

    /**
     * 位置组件：表示实体在二维平面上的坐标。
     */
    static class PositionComponent implements Component {
        private int x;
        private int y;

        public PositionComponent(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * 速度组件：表示实体每一帧在 x/y 方向移动多少。
     */
    static class VelocityComponent implements Component {
        private final int dx;
        private final int dy;

        public VelocityComponent(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    /**
     * 血量组件：表示实体当前生命值。
     */
    static class HealthComponent implements Component {
        private int hp;

        public HealthComponent(int hp) {
            this.hp = hp;
        }
    }

    /**
     * 名称组件：为了让控制台输出更容易阅读。
     */
    static class NameComponent implements Component {
        private final String name;

        public NameComponent(String name) {
            this.name = name;
        }
    }

    /**
     * 世界对象（World）是教学示例里的“总协调者”。
     * 它负责：
     * 1. 创建实体。
     * 2. 存储组件。
     * 3. 提供按组件查询实体的能力。
     */
    static class World {
        private final Set<ECSEntity> entities = new LinkedHashSet<>();

        /**
         * 每一种组件类型，都有一个独立的仓库。
         * key: 组件类型
         * value: 该类型组件对应的 “实体 -> 组件实例” 映射
         */
        private final Map<Class<? extends Component>, Map<ECSEntity, Component>> componentStores = new HashMap<>();

        public ECSEntity createEntity() {
            ECSEntity entity = new ECSEntity();
            entities.add(entity);
            return entity;
        }

        public <T extends Component> void addComponent(ECSEntity entity, T component) {
            componentStores
                    .computeIfAbsent(component.getClass(), key -> new HashMap<>())
                    .put(entity, component);
        }

        @SuppressWarnings("unchecked")
        public <T extends Component> T getComponent(ECSEntity entity, Class<T> componentType) {
            Map<ECSEntity, Component> store = componentStores.get(componentType);
            if (store == null) {
                return null;
            }
            return (T) store.get(entity);
        }

        /**
         * 查询“同时拥有多个组件”的实体。
         * 这是 ECS 中非常核心的操作：系统用它筛选自己要处理的目标。
         */
        @SafeVarargs
        public final Set<ECSEntity> findEntitiesWith(Class<? extends Component>... componentTypes) {
            Set<ECSEntity> result = new LinkedHashSet<>(entities);
            for (Class<? extends Component> componentType : componentTypes) {
                Map<ECSEntity, Component> store = componentStores.get(componentType);
                if (store == null) {
                    result.clear();
                    break;
                }
                result.retainAll(store.keySet());
            }
            return result;
        }

        public Collection<ECSEntity> getEntities() {
            return entities;
        }
    }

    /**
     * 系统接口：所有系统都以 World 为输入。
     * 真实项目中还可能传入 deltaTime、事件总线、物理上下文等。
     */
    interface EcsSystem {
        void update(World world);
    }

    /**
     * 移动系统：
     * 只处理同时拥有 Position 和 Velocity 的实体。
     */
    static class MovementSystem implements EcsSystem {
        @Override
        public void update(World world) {
            for (ECSEntity entity : world.findEntitiesWith(PositionComponent.class, VelocityComponent.class)) {
                PositionComponent position = world.getComponent(entity, PositionComponent.class);
                VelocityComponent velocity = world.getComponent(entity, VelocityComponent.class);
                position.x += velocity.dx;
                position.y += velocity.dy;
            }
        }
    }

    /**
     * 受伤系统：
     * 示例里用一个普通方法演示“系统修改组件数据”的过程。
     */
    static class DamageSystem {
        public void damage(World world, ECSEntity entity, int damage) {
            HealthComponent health = world.getComponent(entity, HealthComponent.class);
            NameComponent name = world.getComponent(entity, NameComponent.class);
            if (health == null) {
                return;
            }
            health.hp = Math.max(0, health.hp - damage);
            String displayName = name == null ? ("实体#" + entity.getId()) : name.name;
            System.out.println(displayName + " 受到 " + damage + " 点伤害，剩余血量: " + health.hp);
        }
    }

    /**
     * 渲染系统：
     * 这里只是打印控制台，目的是让教学示例更直观。
     */
    static class RenderSystem implements EcsSystem {
        @Override
        public void update(World world) {
            for (ECSEntity entity : world.getEntities()) {
                NameComponent name = world.getComponent(entity, NameComponent.class);
                PositionComponent position = world.getComponent(entity, PositionComponent.class);
                HealthComponent health = world.getComponent(entity, HealthComponent.class);

                String displayName = name == null ? ("实体#" + entity.getId()) : name.name;
                String positionText = position == null ? "无位置" : "(" + position.x + ", " + position.y + ")";
                String hpText = health == null ? "无血量" : String.valueOf(health.hp);

                System.out.println(displayName + " | 位置: " + positionText + " | HP: " + hpText);
            }
        }
    }

}
