package animal.utils.greet;

import animal.utils.Range;

import java.time.LocalTime;
import java.util.List;
import java.util.Random;

public class TimedGreeter implements Greeter {

    private final Range<Integer> MORNING = new Range<>(5, 12);
    private final Range<Integer> AFTERNOON = new Range<>(12, 18);

    private final Random random = new Random();

    @Override
    public void hello() {
        int currentHour = LocalTime.now().getHour();

        if (MORNING.isInRange(currentHour)) {
            System.out.println("Good morning!");
        } else if (AFTERNOON.isInRange(currentHour)) {
            System.out.println("Good afternoon!");
        } else {
            System.out.println("Good evening!");
        }
    }

    @Override
    public void goodbye() {
        System.out.println(List.of("Have a nice day!", "See you soon!", "Bye!").get(random.nextInt(3)));
    }
}
