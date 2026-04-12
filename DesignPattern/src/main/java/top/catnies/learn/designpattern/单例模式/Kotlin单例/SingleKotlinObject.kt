package top.catnies.designpattern.单例模式.Kotlin单例


/**
 * Kotlin 自带的 Object 关键字定义的类, 本身就是一个单例.
 * 它会随着 JVM 的加载自动创建, Object 类没有构造方法.
 */
object SingleKotlinObject {
    val name = "This is a Single Kotlin Object"
    
    fun work() {
        println(name)
    }
}


/**
 * 测试运行
 */
fun main(args: Array<String>) {
    // 获取单例内部的字段
    val field = SingleKotlinObject.name
    SingleKotlinObject.work();
}