@FunctionalInterface
public interface RunSomething {
    int doIt(int number);

    static void printName() {
        System.out.println("Kim");
    }

    default void printAge() {
        System.out.println("25");
    }
}
