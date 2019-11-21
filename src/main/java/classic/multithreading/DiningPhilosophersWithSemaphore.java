package classic.multithreading;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophersWithSemaphore {
    public static void main(String[] args) {
        ReentrantLock[] lockList = {new ReentrantLock(),
                new ReentrantLock(),
                new ReentrantLock(),
                new ReentrantLock(),
                new ReentrantLock()};

        Semaphore eatLimit = new Semaphore(3);

        //five philosophers
        PhilosopherWithSemaphore[] philosophers = new PhilosopherWithSemaphore[5];

        //init philosophers and chopsticks
        for (int i = 0; i < 5; ++i) {
            philosophers[i] = new PhilosopherWithSemaphore(i + 1, lockList[i], lockList[(i + 1) % 5], eatLimit);
            philosophers[i].start();
        }
    }
}
