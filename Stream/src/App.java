import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {
        IntroduceStream();
        StreamAPI();
    }

    private static void StreamAPI() {
        // Stream API

        List<OnlineClass> springClasses = new ArrayList<>();
        springClasses.add(new OnlineClass(1, "spring boot", true));
        springClasses.add(new OnlineClass(2, "spring data jpa", true));
        springClasses.add(new OnlineClass(3, "spring mvc", false));
        springClasses.add(new OnlineClass(4, "spring core", false));
        springClasses.add(new OnlineClass(5, "rest api development", false));

        System.out.println("spring으로 시작하는 수업");
        springClasses.stream()
                .filter(oc -> oc.getTitle().startsWith("spring"))
                .forEach(oc -> System.out.println(oc.getId()));

        System.out.println("close 되지 않은 수업");
        springClasses.stream()
                .filter(Predicate.not(OnlineClass::isClosed))
//                .filter(oc -> !oc.isClosed())
                .forEach(oc -> System.out.println(oc.getId()));

        System.out.println("수업 이름만 모아서 스트림 만들기");
        springClasses.stream()
//                .map(oc -> oc.getTitle())
                .map(OnlineClass::getTitle)
//                .forEach(s -> System.out.println(s))
                .forEach(System.out::println);


        List<OnlineClass> javaClasses = new ArrayList<>();
        javaClasses.add(new OnlineClass(6, "The Java, Test", true));
        javaClasses.add(new OnlineClass(6, "The Java, Code manipulation", true));
        javaClasses.add(new OnlineClass(6, "The Java, 8 to 11", false));

        List<List<OnlineClass>> kimEvents = new ArrayList<>();
        kimEvents.add(springClasses);
        kimEvents.add(javaClasses);

        System.out.println("두 수업 목록에 들어있는 모든 수업 아이디 출력");
        kimEvents.stream()
//                .flatMap(list -> list.stream())
                .flatMap(Collection::stream)
                .forEach(oc -> System.out.println(oc.getId()));


        System.out.println("10부터 1씩 증가하는 무제한 스트림 중에서 앞에 10개 빼고 최대 10개까지만");
        Stream.iterate(10, i -> i + 1)
                .skip(10)
                .limit(10)
                .forEach(System.out::println);


        System.out.println("자바 수업 중에 Test가 들어있는 수업이 있는지 확인");
        boolean test = javaClasses.stream().anyMatch(oc -> oc.getTitle().contains("Test"));
        System.out.println(test);


        System.out.println("스프링 수업 중에 제목에 spring이 들어간 제목만 모아서 List로 만들기");
//        List<String> spring = springClasses.stream()
//                .filter(oc -> oc.getTitle().contains("spring"))
//                .map(OnlineClass::getTitle)
//                .collect(Collectors.toList());
//
        List<String> spring = springClasses.stream()
                .map(OnlineClass::getTitle)
                .filter(title -> title.contains("spring"))
                .collect(Collectors.toList());
        spring.forEach(System.out::println);
    }

    private static void IntroduceStream() {
        List<String> names = new ArrayList<>();
        names.add("kim");
        names.add("park");
        names.add("foo");
        names.add("toby");

        // names의 데이터가 바뀌는게 아님
        Stream<String> stringStream = names.stream().map(String::toUpperCase);

        List<String> collect = names.stream().map(s -> {
            System.out.println(s);
            return s.toUpperCase();
        }).collect(Collectors.toList());
        collect.forEach(System.out::println);


        // 병렬처리 가능
        // 병렬처리가 무조건 빠른건 아님
        // 병렬처리를 위해 스레드를 생성하는 비용, 컨텍스트 스위치하는 비용, 수집 비용 등을 생각해야함
        // 데이터가 정말 방대한 경우 사용
        // 성능 측정을 해보아야 알 수 있음
        List<String> collect1 = names.parallelStream().map(s -> {
                    System.out.println(s + " " + Thread.currentThread().getName());
                    return s.toUpperCase();
                }).collect(Collectors.toList());
    }
}
