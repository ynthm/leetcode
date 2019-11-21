package classic.multithreading;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 哲学家
 */
public class Philosopher extends Thread {
    private Chopstick first, second;
    private Random random;

    public Philosopher(Chopstick left, Chopstick right) {
        if (left.getId() < right.getId()) {
            first = left;
            second = right;
        } else {
            first = right;
            second = left;
        }
        random = new Random();
    }

    public void run() {
        try {
            while (true) {
                int i = random.nextInt(2) + 1;
                System.out.println("Philosopher " + this + " has thought " + i + " seconds");
                TimeUnit.SECONDS.sleep(i); // Think for a while
                synchronized (first) {                   // Grab first chopstick
                    synchronized (second) {                // Grab second chopstick
                        System.out.println("Philosopher " + this + " eat for a while");
                        TimeUnit.SECONDS.sleep(random.nextInt(3)); // Eat for a while
                    }
                }
            }
        } catch (InterruptedException e) {
        }
    }
}
