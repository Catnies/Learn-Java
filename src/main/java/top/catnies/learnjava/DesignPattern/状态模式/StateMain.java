package top.catnies.designpattern.状态模式;


import lombok.Data;

/**
 * 状态模式和策略模式很像.
 * 其中的区别, 状态模式下事物可能会自动转变状态.
 * 例如游戏的怪物, 会在 待机 -> 搜索 -> 进攻 这几个状态之间切换.
 * 并且每个不同状态下的行为会不太一样.
 */
public class StateMain {
    public static void main(String[] args) {
        Monster monster = new Monster("Goblin", new IdeaState());

        for (int i = 0; i < 10; i++) {
            monster.tick();
        }

    }
}


/**
 * 状态类, 怪物的每个状态都会被封装成一个单独的类.
 */
interface State {
    void tick(Monster monster);
}
class IdeaState implements State {
    @Override
    public void tick(Monster monster) {
        System.out.println("do nothing!");
        monster.setState(new SeachState());
    }
}
class SeachState implements State {
    @Override
    public void tick(Monster monster) {
        System.out.println("searching!");
        monster.setState(new AttackState());
    }
}
class AttackState implements State {
    @Override
    public void tick(Monster monster) {
        System.out.println("attacking!");
        monster.setState(new IdeaState());
    }
}


/**
 * 怪物类, 持有一些状态.
 */
@Data
class Monster {
    private String name;
    private State state;
    public Monster(String name, State state) {
        this.name = name;
        this.state = state;
    }

    public void tick(){
        state.tick(this);
    }

}