package top.catnies.designpattern.建造者模式;

import lombok.Data;

/**
 * 建造者模式
 * 你可以通过 Book.Builder().setA(a).setB(b).build() 这种形式一步一步设置该类的参数, 最后构建.
 */
@Data
public class Book {

    private String name;
    private String author;
    private int page;
    public Book(String name, String author, int page) {
        this.name = name;
        this.author = author;
        this.page = page;
    }

    public static class BookBuilder {
        private String name = "Simple Love";
        private String author = "None";
        private int page = 10;

        public BookBuilder(){}

        public BookBuilder setName(String name){
            this.name = name;
            return this;
        }
        public BookBuilder setAuthor(String author){
            this.author = author;
            return this;
        }
        public BookBuilder setPage(int page){
            this.page = page;
            return this;
        }

        public Book build(){
            return new Book(name, author, page);
        }
    }

}
