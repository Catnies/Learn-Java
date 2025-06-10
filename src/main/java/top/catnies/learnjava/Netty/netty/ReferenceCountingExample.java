package top.catnies.learnjava.Netty.netty;

// 引用计数的安全实现
public class ReferenceCountingExample {

    // 模拟Netty的引用计数机制
    static class SafeByteBuf {
        private byte[] data;
        private int refCount = 1; // 初始引用计数为1
        private boolean released = false;

        public SafeByteBuf(int size) {
            this.data = new byte[size];
            System.out.println("分配了 " + size + " 字节内存，引用计数: " + refCount);
        }

        // 增加引用计数
        public SafeByteBuf retain() {
            if (released) {
                throw new IllegalStateException("不能retain已释放的对象");
            }
            refCount++;
            System.out.println("引用计数增加到: " + refCount);
            return this;
        }

        // 减少引用计数
        public boolean release() {
            if (released) {
                return false;
            }

            refCount--;
            System.out.println("引用计数减少到: " + refCount);

            if (refCount == 0) {
                this.data = null;
                this.released = true;
                System.out.println("引用计数为0，内存已释放");
                return true;
            }
            return false;
        }

        public byte[] getData() {
            if (released) {
                throw new IllegalStateException("访问已释放的内存！");
            }
            return data;
        }

        public int refCount() {
            return refCount;
        }
    }

    static class SafeHandler1 {
        public void process(SafeByteBuf buf) {
            System.out.println("Handler1 处理数据...");

            // 传递给下一个Handler之前，增加引用计数
            buf.retain(); // 引用计数 +1
            new SafeHandler2().process(buf);

            // 自己用完了，减少引用计数
            buf.release(); // 引用计数 -1
        }
    }

    static class SafeHandler2 {
        public void process(SafeByteBuf buf) {
            System.out.println("Handler2 处理数据...");
            try {
                byte[] data = buf.getData(); // 安全访问
                System.out.println("数据长度: " + data.length);
            } finally {
                // 用完后释放
                buf.release(); // 引用计数 -1，如果为0则真正释放内存
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 安全的引用计数示例 ===");
        SafeByteBuf buf = new SafeByteBuf(1024);
        new SafeHandler1().process(buf);

        System.out.println("\n=== 模拟Netty的Handler链 ===");
        SafeByteBuf nettyBuf = new SafeByteBuf(2048);

        // 模拟经过多个Handler
        System.out.println("经过Handler A...");
        nettyBuf.retain(); // Handler A保留引用

        System.out.println("经过Handler B...");
        nettyBuf.retain(); // Handler B保留引用

        System.out.println("Handler A完成处理");
        nettyBuf.release(); // Handler A释放

        System.out.println("Handler B完成处理");
        nettyBuf.release(); // Handler B释放

        System.out.println("原始持有者释放");
        nettyBuf.release(); // 原始持有者释放，此时引用计数为0，真正释放内存
    }
}