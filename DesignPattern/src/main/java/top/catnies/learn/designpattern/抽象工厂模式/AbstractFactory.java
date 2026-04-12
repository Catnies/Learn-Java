package top.catnies.learn.designpattern.抽象工厂模式;

import top.catnies.learn.designpattern.抽象工厂模式.资源.Color;
import top.catnies.learn.designpattern.抽象工厂模式.资源.Fruit;


/**
 * 抽象工厂
 */
public abstract class AbstractFactory {

    public abstract Fruit getFruit(String type);
    public abstract Color getColor(String type);

}
