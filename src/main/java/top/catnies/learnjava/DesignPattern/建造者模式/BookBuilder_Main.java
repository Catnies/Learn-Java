package top.catnies.designpattern.建造者模式;

public class BookBuilder_Main {

    public static void main(String[] args) {

        // 通过调用 BookBuilder 内部类来创建 Book 的对象.
        Book build = new Book.BookBuilder()
                .setName("Hari")
                .setAuthor("James")
                .setPage(500)
                .build();
        System.out.println(build.toString());

    }
}
