import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@ChickenTypeUse("앙념")
public class App {

    public static void main(@ChickenTypeUse("간장") String[] args)
            throws @ChickenTypeUse("간장") RuntimeException {

        // 애노테이션의 변화
        List<@ChickenTypeUse("양념") String> names = List.of("kim");
        ChickenTypeUse[] annotationsByType = App.class.getAnnotationsByType(ChickenTypeUse.class);
        Arrays.stream(annotationsByType).forEach(c -> System.out.println(c.value()));

        ChickenContainer chickenContainer = App.class.getAnnotation(ChickenContainer.class);
        Arrays.stream(chickenContainer.value()).forEach(c -> System.out.println(c.value()));

        // 배열 병렬 정렬
        int size = 1500;
        int[] numbers = new int[size];
        Random random = new Random();
        IntStream.range(0, size).forEach(i -> numbers[i] = random.nextInt());

        long start = System.nanoTime();
        Arrays.sort(numbers);
        System.out.println("serial sorting took " + (System.nanoTime() - start));

        IntStream.range(0, size).forEach(i -> numbers[i] = random.nextInt());
        start = System.nanoTime();
        Arrays.parallelSort(numbers);
        System.out.println("parallel sorting took " + (System.nanoTime() - start));
    }
    static class FeelsLikeChicken<@Chicken @ChickenTypeUse("양념") T> {

        public static <@Chicken @ChickenTypeUse("양념") C> void print(@ChickenTypeUse("간장") C c) {
            System.out.println("c = " + c);
        }
    }
}
