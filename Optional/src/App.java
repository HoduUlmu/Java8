import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class App {
    public static void main(String[] args) {
        List<OnlineClass> springClasses = new ArrayList<>();
        springClasses.add(new OnlineClass(1, "spring boot", true));
        springClasses.add(new OnlineClass(5, "rest api development", false));

        OnlineClass springBoot = new OnlineClass(1, "spring boot", true);

        // optional로 변경 전
        // null 나옴
//      springBoot.getProgress().getStudyDuration();
        // null 체크를 if문으로 수행하다 보면 잊기 쉬움, null 리턴 자체가 문제
        // 로직 처리시 에러 쓰는건 좋은 습관은 아님 -> 에러를 던질 때 스택을 찍는거 자체가 비용임

        Optional<OnlineClass> optional = springClasses.stream()
                .filter(oc -> oc.getTitle().startsWith("spring"))
                .findFirst();

        boolean present = optional.isPresent();
        boolean empty = optional.isEmpty();// 자바 11부터 제공
        System.out.println("present = " + present + "empty = " + empty);

        // 가급적이면 get 말고 다른 방식으로 가져와라

        optional.ifPresent(oc -> System.out.println(oc.getTitle()));

        // 파라미터로 넘겨준 인스턴스를 생성하는 함수는 값이 있던 없던 무조건 실행됨 -> 비용적으로 좋지않음
        // 상수같이 이미 만들어져있는 인스턴스를 참고할 때는 적합
        OnlineClass onlineClass = optional.orElse(createNewClasses());

        // 위와는 다르게 있는 경우엔 supplier 실행안함
        // 동적으로 작업해서 만들어야 할 때 적합
        OnlineClass onlineClass1 = optional.orElseGet(App::createNewClasses);

        // 기본적으로 NoSuchElementException 던짐
        OnlineClass onlineClass2 = optional.orElseThrow();

        Optional<OnlineClass> onlineClass3 =
                optional.filter(oc -> !oc.isClosed());
        System.out.println(onlineClass3.isEmpty());

        Optional<Integer> integer = optional.map(OnlineClass::getId);
        System.out.println(integer.isPresent());


        // map으로 가져오는 타입이 optional일 경우 복잡해짐
       Optional<Optional<Progress>> progress = optional.map(OnlineClass::getProgress);

       // 반환타입이 optional이면 까서? 반환함
        // stream에서의 flatmap과 다름
        Optional<Progress> progress1 = optional.flatMap(OnlineClass::getProgress);


    }

    private static OnlineClass createNewClasses() {
        System.out.println("creating");
        return new OnlineClass(10, "New Class", false);
    }
}
