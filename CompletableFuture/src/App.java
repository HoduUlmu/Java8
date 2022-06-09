import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class App {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        IntroduceConcurrentProgramming();
        Executors();
        CallableAndFuture();
        CompletableFuture();


    }

    private static void CompletableFuture() throws InterruptedException, ExecutionException {
        // CompletableFuture
        // future.get이 블록킹콜

        CompletableFuture<String> future = new CompletableFuture<>();
//        CompletableFuture<String> future1 = CompletableFuture.completedFuture("kim");
        future.complete("kim");
        System.out.println(future.get());

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            System.out.println("Hello" + Thread.currentThread().getName());
        });
        // 에러처리 해줌
        future1.get();


        // 콜백을 get 호출 전에 가능
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello" + Thread.currentThread().getName());
            return "Hello";
        }).thenApply(s -> {
            System.out.println(Thread.currentThread().getName());
            return s.toUpperCase();
        });
        System.out.println(future2.get());

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello" + Thread.currentThread().getName());
            return "Hello";
        });

        // 연관관계가 있는 경우
        CompletableFuture<String> future5 = future3.thenCompose(App::getCompletableFuture);
        System.out.println(future5.get());

        // 연관관계가 없는 경우
        CompletableFuture<String> future4 = CompletableFuture.supplyAsync(() -> {
            System.out.println("World" + Thread.currentThread().getName());
            return "World";
        });

        CompletableFuture<String> future6 = future3.thenCombine(future4, (h, w) -> h + " " + w);
        System.out.println("future6 = " + future6);
    }

    private static CompletableFuture<String> getCompletableFuture(String message) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("World" + Thread.currentThread().getName());
            return message + "World";
        });
    }

    private static void CallableAndFuture() throws InterruptedException, ExecutionException {
        // 리턴값이 없는 경우 runnable, 있는 경우 callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<String> hello = () -> {
            Thread.sleep(2000L);
            return "Hello";
        };

        Future<String> future = executorService.submit(hello);
        System.out.println(future.isDone());
        System.out.println("Started! ");
        future.get(); // 블록킹
//        future.cancel(false);
        System.out.println(future.isDone());
        System.out.println("End! ");

        Callable<String> java = () -> {
            Thread.sleep(3000L);
            return "Hello";
        };

        Callable<String> kim = () -> {
            Thread.sleep(1000L);
            return "Hello";
        };

        // invokeall은 인자로 넘어온 모든 callable이 끝날 때까지 기다림
        List<Future<String>> futures = executorService.invokeAll(Arrays.asList(hello, java, kim));
        for (Future<String> stringFuture : futures) {
            System.out.println(stringFuture.get());
        }

        // 인자로 넘어온 callable 중 제일 먼저 끝난 결과값 리턴, 블록킹콜
        // singleThreadpool의 경우 hello가 리턴, 3개 이상의 threadpool의 경우 제일 짧은 kim 리턴
        ExecutorService executorService2 = Executors.newFixedThreadPool(4);
        String s = executorService.invokeAny(Arrays.asList(hello, java, kim));
        String s2 = executorService2.invokeAny(Arrays.asList(hello, java, kim));
        System.out.println(s);
        System.out.println(s2);
    }

    private static void Executors() {
        // 고수준 concurrency 프로그래밍
        // runnable만 제공해주면됨
        // 쓰레드를 만들고 없애고 하는 일련의 작업들은 대신해줌

        // 익스큐터서비스는 작업을 하나 끝내고 나면 다음 작업까지 대기함 종료 x
        // 명시적 shutdown 필요
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> System.out.println("Thread" + Thread.currentThread().getName()));
        executorService.submit(() -> System.out.println("Thread" + Thread.currentThread().getName()));
        executorService.shutdown();         // graceful shutdown -> 현재 진행중인 작업은 마치고 끝냄
        executorService.shutdownNow(); // 바로 셧다운

        ExecutorService executorService1 = Executors.newFixedThreadPool(2);
        executorService1.submit(getRunnable("Hello"));
        executorService1.submit(getRunnable("Kim"));
        executorService1.submit(getRunnable("The"));
        executorService1.submit(getRunnable("Java"));
        executorService1.submit(getRunnable("Hi"));
        executorService1.shutdown();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(getRunnable("hello"), 3, TimeUnit.SECONDS);
        scheduledExecutorService.scheduleAtFixedRate(getRunnable("hello"), 1, 2, TimeUnit.SECONDS);
    }

    private static Runnable getRunnable(String message) {
        return () -> System.out.println(message + Thread.currentThread().getName());
    }

    private static void IntroduceConcurrentProgramming() {
        MyThread myThread = new MyThread();
        myThread.start();
        System.out.println("Hello");


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread" + Thread.currentThread().getName());
            }
        });
        thread.start();

        // 자바 8 이후 람다활용
        // runnable이 functional interface로 바뀜
        Thread thread2 = new Thread(() -> {
            while (true) {
                System.out.println("Thread" + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    System.out.println("exit!");
                    return; // 없으면 에러 던지고 계속 진행함
                }

            }
        });
        thread.start();
        System.out.println("Hello" + Thread.currentThread().getName());
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.interrupt(); // 종료하라는 메소드가 아니라 방해할 뿐임

        Thread thread3 = new Thread(() -> {
            System.out.println("Thread" + Thread.currentThread().getName());
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });
        thread3.start();

        System.out.println("Hello" + Thread.currentThread().getName());
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(thread3 + "is finished");
    }

    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Thread " + Thread.currentThread().getName());
        }
    }
}

