package top.catnies.learnjava.ClassLoader;

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
        return "Resource{" + "namehahaa='" + name + '\'' + '}';
    }
}
