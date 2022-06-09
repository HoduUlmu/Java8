import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class App {
    public static void main(String[] args) {

        // 기존 시간과 관련된 API들은 여러 문제가 있음
        // 이름이 직관성이 꽝임
        // 대표적으로 멀티스레드에서 안전하지 않음
        // GregorianCalendar의 경우 타입 안전성이 없음
       Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat();


        // 자바 8 이후 시간 API는 기계용 시간과 사람용 시간으로 나눌 수 있음

        // 기계적 시간
        // 메소드 실행시간 비교같은 때 쓰임
        Instant instant = Instant.now();
        System.out.println("now = " + instant); // 기준시 UTC, GMT
        System.out.println("now = " + instant.atZone(ZoneId.of("UTC")));

        ZoneId zoneId = ZoneId.systemDefault();
        System.out.println("zoneId = " + zoneId);
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        System.out.println("zonedDateTime = " + zonedDateTime);


        // 사람용 시간
        LocalDateTime now = LocalDateTime.now();
        System.out.println("now = " + now); // 서버 배포시 서버가 미국에 있으면 그 시간을 가져오게됨
        LocalDateTime of
                = LocalDateTime.of(1982, Month.APRIL, 15, 0, 0, 10);

        ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        System.out.println("nowInKorea = " + nowInKorea);


        // 기간
        LocalDate today = LocalDate.now();
        LocalDate of1 = LocalDate.of(2020, Month.DECEMBER, 15);

        Period between = Period.between(today, of1);
        System.out.println(between.getDays());
        Period until = today.until(of1);
        System.out.println(until.get(ChronoUnit.DAYS));

        Instant instant1 = Instant.now();
        Instant plus1 = instant1.plus(10, ChronoUnit.SECONDS);
        Duration between1 = Duration.between(instant1, plus1);
        System.out.println("between1 = " + between1);


        // 포맷팅
        LocalDateTime dateTime = LocalDateTime.now();
        System.out.println(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        System.out.println(dateTime.format(dateTimeFormatter));

        LocalDate parse = LocalDate.parse("07/15/1982", dateTimeFormatter);
        System.out.println(parse);

        // 레거시 호환
        Date date2 = new Date();
        Instant instant2 = date2.toInstant();
        Date from = Date.from(instant2);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        LocalDateTime dateTime1 =
                gregorianCalendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime dt = gregorianCalendar.toZonedDateTime().toLocalDateTime();
    }
}
