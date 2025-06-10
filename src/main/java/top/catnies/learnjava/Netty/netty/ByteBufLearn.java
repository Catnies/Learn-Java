package top.catnies.learnjava.Netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

public class ByteBufLearn {
    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer(24);
        System.out.println("初始状态：" + Arrays.toString(buf.array()));

        buf.writeInt(66); // 写入一个Int数据.
        System.out.println("写入Int后：" + Arrays.toString(buf.array()));

        System.out.println(buf.readInt()); //无需翻转，直接读取数据出来.
        System.out.println("读取Int后："+ Arrays.toString(buf.array()));

        buf.discardReadBytes(); // 丢弃操作，会将当前的可读部分内容丢到最前面，并且读写指针向前移动丢弃的距离
        System.out.println("丢弃之后："+Arrays.toString(buf.array()));

        buf.clear();    //清空操作，清空之后读写指针都归零
        System.out.println("清空之后："+Arrays.toString(buf.array()));
    }
}
