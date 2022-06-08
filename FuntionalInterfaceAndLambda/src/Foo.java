import java.util.Arrays;
import java.util.function.*;

public class Foo {
    public static void main(String[] args) {
        FunctionalInterfaceAndLambdaIntroduce();
        FunctionalInterfacesJavaProvide();
        LambdaExpression();

        // 4. 메소드 레퍼런스

        // 구현체로 static 메소드를 쓰겠다
        UnaryOperator<String> hi = Greeting::hi;

        // 인스턴스 메소드 참조
        Greeting greeting = new Greeting();
        UnaryOperator<String> hello = greeting::hello;
        System.out.println(hello.apply("Kim"));

        // 생성자 참조
        // 메소드 레퍼런스만 보고 어떤 생성자 참조하는지 알 수 없음
        Supplier<Greeting> newGreeting = Greeting::new;
        newGreeting.get(); // 인스턴스 생성

        Function<String, Greeting> kimGreeting = Greeting::new;
        kimGreeting.apply("kim"); // 인스턴스 생성


        // 임의 객체의 인스턴스 메소드 참조 static 메소드 아님
        String[] names = {"kim", "toby"};
        Arrays.sort(names, String::compareToIgnoreCase);
        System.out.println(Arrays.toString(names));
    }

    private static void LambdaExpression() {
        // 3. 람다 표현식
        // 변수 캡처
        Foo foo = new Foo();
        foo.run();
    }

    private void run() {
        int baseNum = 10; // 자바 8에선 사실상 final? 경우 생략 가능, effective final 알아보기

        // 람다
        // 쉐도잉 안됨, 람다는 스코프가 람다를 감싸고 있는 메소드(run)과 같음
        IntConsumer printInt = (i) -> {
//          int baseNum = 11; 스코프가 같아 정의안됨
            System.out.println(i + baseNum);
        };
        printInt.accept(10);


        // 로컬 클래스
        // 쉐도잉됨
        class LocalClass {
            void printBaseNum() {
                int baseNum = 11;
                System.out.println(baseNum);
            }
        }

        // 익명 클래스
        // 쉐도잉됨
        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer baseNum) {
                System.out.println(baseNum);
            }
        };
    }

    private static void FunctionalInterfacesJavaProvide() {
        // 2. 자바에서 제공하는 함수형 인터페이스

        // 2-1. Function<T, R>
        Plus10 plus10 = new Plus10();
        System.out.println(plus10.apply(1));

        Function<Integer, Integer> plus10Lambda = (number) -> number + 10;
        System.out.println(plus10Lambda.apply(1));


        Function<Integer, Integer> multiply2 = (number) -> number * 2;
        // 함수 조합
        // compose -> 매개변수로 넘긴 함수를 실행한 리턴값을 매개변수로 사용 후 리턴
        // multiply2 이후 plus10
        Function<Integer, Integer> multiply2AndPlus10 = plus10Lambda.compose(multiply2);
        System.out.println(multiply2AndPlus10.apply(2));

        // andThen -> 매개변수를 받아 자기 자신을 실행 후 리턴값을 매개변수로 넣은 함수에 입력 후 리턴
        // plus10 이후 multiply2
        System.out.println(plus10Lambda.andThen(multiply2).apply(2));


        // 2-2. Consumer<T> 리턴값 x
        Consumer<Integer> printT = (number) -> System.out.println(number);
        printT.accept(10);


        // 2-3. Supplier<T>
        Supplier<Integer> get10 = () -> 10;
        System.out.println(get10);

        // 2-4. Predicate<T> true, false 반환, 조합 가능
        Predicate<String> startsWithKim = (s) -> s.startsWith("Kim");
        Predicate<Integer> isEven = (i) -> i / 2 == 0;

        // 2-5. Unary 입력값과 결과값의 타입이 같은 경우, Function 상속받음
        UnaryOperator<Integer> plus5 = (i) -> i + 5;
    }


    private static void FunctionalInterfaceAndLambdaIntroduce() {
        // 1. 함수형 인터페이스와 람다 표현식 소개
        int baseNumber = 1; // 외부값 변경 x, 참조 o (final 일때만)

        // 익명 내부 클래스 anonymous inner class 자바 8 이전
        RunSomething runSomething = new RunSomething() {
            // int baseNumber = 1; 내부에 상태값이 있으면 퓨어한 함수가 아님

            @Override
            public int doIt(int number) {
                System.out.println("Hello");
                System.out.println("Lambda");
                return number + 10;
            }
        };

        // 람다 표현식 자바 8
        // 함수형 인터페이스를 인라인으로 구현한 오브젝트
        RunSomething runSomething1 = (number) -> {
            System.out.println("Hello");
            System.out.println("Lambda");
            return number + 10;
        };

        runSomething.doIt(10);
        runSomething1.doIt(10);
    }
}
