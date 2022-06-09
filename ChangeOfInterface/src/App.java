import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;

public class App {
    public static void main(String[] args) {
        Foo foo = new DefaultFoo("Kim");
        foo.printName();
        foo.printNameUpperCase();

        Foo.printAnything();


        // 자바 8에서 추가된 디폴트 메소드로 인한 API 변화
        List<String> name = new ArrayList<>();
        name.add("kim");
        name.add("park");
        name.add("foo");
        name.add("toby");

        name.forEach(System.out::println);
        
        Spliterator<String> spliterator = name.spliterator();
        while (spliterator.tryAdvance(System.out::println));
        Spliterator<String> spliterator1 = spliterator.trySplit();
        while (spliterator.tryAdvance(System.out::println));
        while (spliterator1.tryAdvance(System.out::println));

        long k = name.stream()
                .map(String::toUpperCase)
                .filter(s -> s.startsWith("k"))
                .count();
        System.out.println("k = " + k);

        name.removeIf(s -> s.startsWith("k"));
        name.forEach(System.out::println);

        Comparator<String> compareToIgnoreCase = String::compareToIgnoreCase;
        name.sort(compareToIgnoreCase.reversed().thenComparing(String::compareTo));


    }
}
