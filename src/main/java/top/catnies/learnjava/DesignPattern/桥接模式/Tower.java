package top.catnies.designpattern.桥接模式;

/**
 * 桥接模式旨在解决组合的问题.
 * 例如有个塔防游戏, 你有若干种防御塔, 还有若干种武器.
 * 防御塔可以和不同的武器进行不同的组合.
 * 这个时候要实现任意防御塔和任意武器的组合时, 则可以使用 桥接模式 进行.
 */


/**
 * 创建一个炮塔的抽象类, 这个抽象类定义了炮塔的默认构造参数, 它需要一个武器安装给炮塔.
 */
abstract class AbstractTower{
    Weapon weapon;
    AbstractTower(Weapon weapon){
        this.weapon = weapon;
    }
    abstract void run();
}
/**
 * 创建炮塔, 炮塔需要继承抽象炮塔, 然后在攻击方法中直接使用内部的物品攻击.
 */
class NormalTower extends AbstractTower {
    NormalTower(Weapon weapon) {
        super(weapon);
    }
    @Override
    public void run() {
        weapon.attack();
    }
}
class AdvanceTower extends AbstractTower {
    AdvanceTower(Weapon weapon) {
        super(weapon);
    }
    @Override
    public void run() {
        weapon.attack();
    }
}




/**
 * 武器类相关, 并且创建了 枪 和 激光 两种武器.
 */
interface Weapon{
    void attack();
}
class Gun implements Weapon{
    @Override
    public void attack() {
        System.out.println("Shoot!");
    }
}
class Laser implements Weapon{
    @Override
    public void attack() {
        System.out.println("Zap!");
    }
}


/**
 * 自动组合类
 */
class TowerManager {
    // 获取武器
    private static Weapon getWeapon(String weaponType) {
        return switch (weaponType.toLowerCase()) {
            case "gun" -> new Gun();
            case "laser" -> new Laser();
            default -> throw new IllegalArgumentException("Unknown weapon type: " + weaponType);
        };
    }

    // 获取防御塔
    private static AbstractTower getTowerInstance(String towerType, Weapon weapon) {
        return switch (towerType.toLowerCase()) {
            case "normal" -> new NormalTower(weapon);
            case "advance" -> new AdvanceTower(weapon);
            default -> throw new IllegalArgumentException("Unknown tower type: " + towerType);
        };
    }

    // 获取塔与武器的组合
    public static AbstractTower getTower(String towerType, String weaponType) {
        Weapon weapon = getWeapon(weaponType);
        return getTowerInstance(towerType, weapon);
    }
}


/**
 * 测试代码
 */
class main {
    public static void main(String[] args) {
        // 利用桥接可以创建一个 普通防御塔 + 激光武器 的塔.
        // 然后使用激光武器进行攻击
        Laser laser = new Laser();
        NormalTower normalTower = new NormalTower(laser);
        normalTower.run();


        // 同样也可以让 普通防御塔 组合 枪械武器.
        Gun gun = new Gun();
        NormalTower normalTower1 = new NormalTower(gun);
        normalTower1.run();


        // 和JDBC类似, 使用静态方法自动组合
        AbstractTower tower = TowerManager.getTower("normal", "gun");
        tower.run();
    }
}