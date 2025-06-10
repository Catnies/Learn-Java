package top.catnies.learnjava.类加载器;

public class Resource {

    private String name;

    public Resource(String name) {
        this.name = name;
        System.out.println("Resource init");
    }

    public Resource() {
        this.name = "default";
        System.out.println("Resource init");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Resource{" + "name888='" + name + '\'' + '}';
    }
}
